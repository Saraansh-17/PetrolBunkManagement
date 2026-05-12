import mongoose, { Schema, type Document } from 'mongoose';

export interface ISaleDocument extends Document {
  pumpId: string;
  employeeId: string;
  fuelType: string;
  litersSold: number;
  pricePerLiter: number;
  totalAmount: number;
  paymentMode: string;
  timestamp: Date;
}

const SaleSchema: Schema = new Schema({
  pumpId: { type: String, required: true },
  employeeId: { type: String, required: true },
  fuelType: { type: String, required: true, enum: ['PETROL', 'DIESEL'] },
  litersSold: { type: Number, required: true },
  pricePerLiter: { type: Number, required: true },
  totalAmount: { type: Number, required: true },
  paymentMode: { type: String, required: true, enum: ['CASH', 'CARD', 'UPI'] },
  timestamp: { type: Date, default: Date.now }
}, {
  timestamps: true
});

export const SaleModel = mongoose.model<ISaleDocument>('Sale', SaleSchema);
