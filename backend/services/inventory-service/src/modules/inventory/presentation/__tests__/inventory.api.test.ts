import { describe, it, expect, vi, beforeEach, beforeAll, afterAll } from 'vitest';
import request from 'supertest';
import app from '../../../../app.js';
import mongoose from 'mongoose';
import { connectDB } from '../../../../config/db.js';
import { InventoryModel } from '../../infrastructure/persistence/mongoose/inventory.model.js';
import { ReservationModel } from '../../infrastructure/persistence/mongoose/reservation.model.js';
import { createClient } from 'redis';

describe('Inventory API', () => {
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
        // clear cache and DB before each test
        await testRedisClient.del('inventory:stock:PETROL');
        await InventoryModel.deleteMany({});
        await ReservationModel.deleteMany({});
    });

    describe('GET /api/v1/inventory/stock', () => {
        it('should return 400 for invalid fuel type', async () => {
            const res = await request(app).get('/api/v1/inventory/stock?fuelType=INVALID');
            
            expect(res.status).toBe(400);
            expect(res.body.error).toBe('Bad Request');
            expect(res.body.message).toBe('Invalid fuel type provided');
        });

        it('should return stock info successfully', async () => {
            // Because of how vi.mock works with ES6 classes instantiated in another file, 
            // we should mock the prototype or use the mock instance.
            // A simple way to control the mock from here when the route instantiates it is to mock the prototype:
            // Since DB is cleared, first request seeds the DB
            const res = await request(app).get('/api/v1/inventory/stock?fuelType=PETROL');
            
            expect(res.status).toBe(200);
            expect(res.body.fuelType).toBe('PETROL');
            expect(res.body.availableLiters).toBe(10000);
            expect(res.body.source).toBe('DATABASE');
            
            // Second request should hit the real Redis cache
            const res2 = await request(app).get('/api/v1/inventory/stock?fuelType=PETROL');
            expect(res2.status).toBe(200);
            expect(res2.body.source).toBe('REDIS');
        });
    });

    describe('POST /api/v1/inventory/reserve', () => {
        it('should return 400 if fields are missing', async () => {
            const res = await request(app)
                .post('/api/v1/inventory/reserve')
                .send({
                    fuelType: 'PETROL',
                    // missing quantity and referenceId
                });
            
            expect(res.status).toBe(400);
            expect(res.body.message).toBe('Validation failed: Missing fields');
        });

        it('should reserve stock successfully', async () => {
            // First seed the inventory
            await request(app).get('/api/v1/inventory/stock?fuelType=PETROL');

            const res = await request(app)
                .post('/api/v1/inventory/reserve')
                .send({
                    fuelType: 'PETROL',
                    quantity: 1000,
                    referenceId: 'ref-123'
                });

            expect(res.status).toBe(200);
            expect(res.body.reservationId).toBeDefined();
            expect(res.body.status).toBe('RESERVED');
            
            // Verify cache was updated by reserveStock
            const res2 = await request(app).get('/api/v1/inventory/stock?fuelType=PETROL');
            expect(res2.status).toBe(200);
            expect(res2.body.source).toBe('REDIS');
            expect(res2.body.availableLiters).toBe(9000);
        });

        it('should return 409 Conflict if stock is insufficient', async () => {
            // First seed the inventory to 10000
            await request(app).get('/api/v1/inventory/stock?fuelType=PETROL');

            const res = await request(app)
                .post('/api/v1/inventory/reserve')
                .send({
                    fuelType: 'PETROL',
                    quantity: 100000,
                    referenceId: 'ref-123'
                });

            expect(res.status).toBe(409);
            expect(res.body.error).toBe('Conflict');
            expect(res.body.message).toBe('Unable to reserve fuel stock');
        });
    });
});
