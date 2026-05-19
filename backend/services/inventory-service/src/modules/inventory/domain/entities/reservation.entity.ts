export interface Reservation {
    reservationId: string;
    referenceId: string;
    fuelType: 'PETROL' | 'DIESEL';
    quantity: number;
    status: 'RESERVED' | 'COMPLETED' | 'CANCELLED';
    createdAt: Date;
    updatedAt: Date;
}
