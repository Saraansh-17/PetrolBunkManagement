import mongoose, { Schema, Document } from 'mongoose';
import { Inventory } from '../../../domain/entities/inventory.entity.js';

export interface InventoryDocument extends Document, Omit<Inventory, 'updatedAt'> {
    updatedAt: Date;
}

const inventorySchema = new Schema<InventoryDocument>({
    fuelType: { type: String, required: true, enum: ['PETROL', 'DIESEL'], unique: true },
    availableLiters: { type: Number, required: true, default: 0 },
    reservedLiters: { type: Number, required: true, default: 0 }
}, {
    timestamps: true
});

export const InventoryModel = mongoose.model<InventoryDocument>('Inventory', inventorySchema);
