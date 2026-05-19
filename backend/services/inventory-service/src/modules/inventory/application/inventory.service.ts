import { IInventoryRepository } from '../domain/interfaces/inventory-repository.interface.js';
import { ICacheService } from '../domain/interfaces/cache-service.interface.js';

export class InventoryService {
    constructor(
        private inventoryRepo: IInventoryRepository,
        private cacheService: ICacheService
    ) {}

    private getCacheKey(fuelType: string): string {
        return `inventory:stock:${fuelType}`;
    }

    async getStock(fuelType: string): Promise<{ fuelType: string; availableLiters: number; source: 'REDIS' | 'DATABASE' }> {
        const cacheKey = this.getCacheKey(fuelType);
        
        // Try Redis first
        const cachedStock = await this.cacheService.get(cacheKey);
        if (cachedStock !== null) {
            return {
                fuelType,
                availableLiters: parseFloat(cachedStock),
                source: 'REDIS'
            };
        }

        // Fallback to database
        const inventory = await this.inventoryRepo.getInventory(fuelType);
        if (!inventory) {
            // Seed default if not exists
            const initial = await this.inventoryRepo.initializeInventory(fuelType, 10000); // 10000 initial for dev
            await this.cacheService.set(cacheKey, initial.availableLiters.toString());
            return {
                fuelType,
                availableLiters: initial.availableLiters,
                source: 'DATABASE'
            };
        }

        // Populate Cache
        await this.cacheService.set(cacheKey, inventory.availableLiters.toString());

        return {
            fuelType,
            availableLiters: inventory.availableLiters,
            source: 'DATABASE'
        };
    }

    async reserveStock(fuelType: string, quantity: number, referenceId: string): Promise<{ reservationId: string; status: string }> {
        const reservation = await this.inventoryRepo.reserveStock(fuelType, quantity, referenceId);
        
        // Invalidate or update cache after successful DB reserve
        const cacheKey = this.getCacheKey(fuelType);
        const inventory = await this.inventoryRepo.getInventory(fuelType);
        if (inventory) {
            await this.cacheService.set(cacheKey, inventory.availableLiters.toString());
        } else {
            await this.cacheService.del(cacheKey);
        }

        return {
            reservationId: reservation.reservationId,
            status: reservation.status
        };
    }

    async updateStock(fuelType: string, amount: number): Promise<void> {
        await this.inventoryRepo.updateStock(fuelType, amount);
        
        // Update cache after async DB update
        const cacheKey = this.getCacheKey(fuelType);
        const inventory = await this.inventoryRepo.getInventory(fuelType);
        if (inventory) {
            await this.cacheService.set(cacheKey, inventory.availableLiters.toString());
        }
    }
}
