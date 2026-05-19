import mongoose, { Schema, Document } from 'mongoose';
import { Reservation } from '../../../domain/entities/reservation.entity.js';

export interface ReservationDocument extends Document, Omit<Reservation, 'reservationId' | 'createdAt' | 'updatedAt'> {
    reservationId: string;
    createdAt: Date;
    updatedAt: Date;
}

const reservationSchema = new Schema<ReservationDocument>({
    reservationId: { type: String, required: true, unique: true },
    referenceId: { type: String, required: true },
    fuelType: { type: String, required: true, enum: ['PETROL', 'DIESEL'] },
    quantity: { type: Number, required: true },
    status: { type: String, required: true, enum: ['RESERVED', 'COMPLETED', 'CANCELLED'] }
}, {
    timestamps: true
});

export const ReservationModel = mongoose.model<ReservationDocument>('Reservation', reservationSchema);
