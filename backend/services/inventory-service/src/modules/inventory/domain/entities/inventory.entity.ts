export interface Inventory {
    fuelType: 'PETROL' | 'DIESEL';
    availableLiters: number;
    reservedLiters: number;
    updatedAt: Date;
}
