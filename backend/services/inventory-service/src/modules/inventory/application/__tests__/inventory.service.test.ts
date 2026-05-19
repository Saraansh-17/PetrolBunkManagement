import { describe, it, expect, vi, beforeEach, beforeAll, afterAll } from 'vitest';
import mongoose from 'mongoose';
import { createClient } from 'redis';
import { connectDB } from '../../../../config/db.js';
import { InventoryService } from '../inventory.service.js';
import { MongooseInventoryRepository } from '../../infrastructure/persistence/mongoose/mongoose-inventory.repository.js';
import { RedisCacheService } from '../../infrastructure/cache/redis-cache.service.js';
import { InventoryModel } from '../../infrastructure/persistence/mongoose/inventory.model.js';
import { ReservationModel } from '../../infrastructure/persistence/mongoose/reservation.model.js';

describe('InventoryService Integration Tests', () => {
    let inventoryService: InventoryService;
    let testRedisClient: any;

    beforeAll(async () => {
        await connectDB();
        testRedisClient = createClient({ url: process.env.REDIS_URL || 'redis://localhost:6379' });
        await testRedisClient.connect();
    });

    afterAll(async () => {
        await mongoose.disconnect();
        await testRedisClient.quit();
    });

    beforeEach(async () => {
        vi.clearAllMocks();
        
        // Clear real DB and Cache
        await testRedisClient.del('inventory:stock:PETROL');
        await testRedisClient.del('inventory:stock:DIESEL');
        await InventoryModel.deleteMany({});
        await ReservationModel.deleteMany({});

        // Initialize Real Services
        const inventoryRepo = new MongooseInventoryRepository();
        const cacheService = new RedisCacheService();
        inventoryService = new InventoryService(inventoryRepo, cacheService);
    });

    describe('getStock', () => {
        it('should initialize inventory if DB miss and cache miss', async () => {
            const result = await inventoryService.getStock('PETROL');
            
            expect(result.fuelType).toBe('PETROL');
            expect(result.availableLiters).toBe(10000); // our default seed
            expect(result.source).toBe('DATABASE');

            // Should also be in real DB
            const dbInv = await InventoryModel.findOne({ fuelType: 'PETROL' });
            expect(dbInv).not.toBeNull();
            expect(dbInv?.availableLiters).toBe(10000);

            // Should also be in real Cache
            const cacheVal = await testRedisClient.get('inventory:stock:PETROL');
            expect(cacheVal).toBe('10000');
        });

        it('should return stock from cache if available', async () => {
            // Pre-seed cache
            await testRedisClient.set('inventory:stock:PETROL', '5000');

            const result = await inventoryService.getStock('PETROL');
            
            expect(result.fuelType).toBe('PETROL');
            expect(result.availableLiters).toBe(5000);
            expect(result.source).toBe('REDIS');
        });
    });

    describe('reserveStock', () => {
        it('should reserve stock successfully and update cache', async () => {
            // Pre-seed DB directly
            await InventoryModel.create({
                fuelType: 'PETROL',
                availableLiters: 10000,
                reservedLiters: 0
            });

            const result = await inventoryService.reserveStock('PETROL', 100, 'ref1');

            expect(result.reservationId).toBeDefined();
            expect(result.status).toBe('RESERVED');

            // Verify Cache updated
            const cacheVal = await testRedisClient.get('inventory:stock:PETROL');
            expect(cacheVal).toBe('9900');

            // Verify DB updated
            const dbInv = await InventoryModel.findOne({ fuelType: 'PETROL' });
            expect(dbInv?.availableLiters).toBe(9900);
            expect(dbInv?.reservedLiters).toBe(100);
        });

        it('should throw error if reservation fails due to insufficient stock', async () => {
            // Pre-seed DB with very low stock
            await InventoryModel.create({
                fuelType: 'PETROL',
                availableLiters: 50,
                reservedLiters: 0
            });

            await expect(inventoryService.reserveStock('PETROL', 1000, 'ref1'))
                .rejects.toThrow('Insufficient stock');
        });
    });
});
