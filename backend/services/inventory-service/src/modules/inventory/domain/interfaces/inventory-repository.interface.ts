import { Inventory } from '../entities/inventory.entity.js';
import { Reservation } from '../entities/reservation.entity.js';

export interface IInventoryRepository {
    getInventory(fuelType: string): Promise<Inventory | null>;
    initializeInventory(fuelType: string, initialStock: number): Promise<Inventory>;
    reserveStock(fuelType: string, quantity: number, referenceId: string): Promise<Reservation>;
    confirmReservation(reservationId: string): Promise<void>;
    cancelReservation(reservationId: string): Promise<void>;
    updateStock(fuelType: string, amount: number): Promise<void>;
}
