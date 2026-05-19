import { IInventoryRepository } from '../../../domain/interfaces/inventory-repository.interface.js';
import { Inventory } from '../../../domain/entities/inventory.entity.js';
import { Reservation } from '../../../domain/entities/reservation.entity.js';
import { InventoryModel } from './inventory.model.js';
import { ReservationModel } from './reservation.model.js';
import mongoose from 'mongoose';
import { randomUUID } from 'crypto';

export class MongooseInventoryRepository implements IInventoryRepository {
    
    async getInventory(fuelType: string): Promise<Inventory | null> {
        const inventory = await InventoryModel.findOne({ fuelType: fuelType as 'PETROL' | 'DIESEL' });
        if (!inventory) return null;
        
        return {
            fuelType: inventory.fuelType as 'PETROL' | 'DIESEL',
            availableLiters: inventory.availableLiters,
            reservedLiters: inventory.reservedLiters,
            updatedAt: inventory.updatedAt
        };
    }

    async initializeInventory(fuelType: string, initialStock: number): Promise<Inventory> {
        const inventory = new InventoryModel({
            fuelType,
            availableLiters: initialStock,
            reservedLiters: 0
        });
        await inventory.save();
        return {
            fuelType: inventory.fuelType as 'PETROL' | 'DIESEL',
            availableLiters: inventory.availableLiters,
            reservedLiters: inventory.reservedLiters,
            updatedAt: inventory.updatedAt
        };
    }

    async reserveStock(fuelType: string, quantity: number, referenceId: string): Promise<Reservation> {
        const session = await mongoose.startSession();
        session.startTransaction();
        try {
            const inventory = await InventoryModel.findOneAndUpdate(
                { fuelType: fuelType as 'PETROL' | 'DIESEL', availableLiters: { $gte: quantity } },
                { 
                    $inc: { 
                        availableLiters: -quantity, 
                        reservedLiters: quantity 
                    } 
                },
                { new: true, session }
            );

            if (!inventory) {
                throw new Error('Insufficient stock or fuel type not found');
            }

            const reservationId = `RES${Date.now()}${Math.floor(Math.random() * 1000)}`;
            const reservation = new ReservationModel({
                reservationId,
                referenceId,
                fuelType,
                quantity,
                status: 'RESERVED'
            });

            await reservation.save({ session });
            await session.commitTransaction();
            
            return {
                reservationId: reservation.reservationId,
                referenceId: reservation.referenceId,
                fuelType: reservation.fuelType as 'PETROL' | 'DIESEL',
                quantity: reservation.quantity,
                status: reservation.status as 'RESERVED',
                createdAt: reservation.createdAt,
                updatedAt: reservation.updatedAt
            };
        } catch (error) {
            await session.abortTransaction();
            throw error;
        } finally {
            session.endSession();
        }
    }

    async confirmReservation(reservationId: string): Promise<void> {
        const session = await mongoose.startSession();
        session.startTransaction();
        try {
            const reservation = await ReservationModel.findOneAndUpdate(
                { reservationId, status: 'RESERVED' },
                { status: 'COMPLETED' },
                { session }
            );

            if (!reservation) {
                throw new Error('Reservation not found or not in RESERVED state');
            }

            await InventoryModel.findOneAndUpdate(
                { fuelType: reservation.fuelType },
                { $inc: { reservedLiters: -reservation.quantity } },
                { session }
            );

            await session.commitTransaction();
        } catch (error) {
            await session.abortTransaction();
            throw error;
        } finally {
            session.endSession();
        }
    }

    async cancelReservation(reservationId: string): Promise<void> {
        const session = await mongoose.startSession();
        session.startTransaction();
        try {
            const reservation = await ReservationModel.findOneAndUpdate(
                { reservationId, status: 'RESERVED' },
                { status: 'CANCELLED' },
                { session }
            );

            if (!reservation) {
                throw new Error('Reservation not found or not in RESERVED state');
            }

            await InventoryModel.findOneAndUpdate(
                { fuelType: reservation.fuelType },
                { 
                    $inc: { 
                        availableLiters: reservation.quantity,
                        reservedLiters: -reservation.quantity 
                    } 
                },
                { session }
            );

            await session.commitTransaction();
        } catch (error) {
            await session.abortTransaction();
            throw error;
        } finally {
            session.endSession();
        }
    }

    async updateStock(fuelType: string, amount: number): Promise<void> {
        await InventoryModel.findOneAndUpdate(
            { fuelType: fuelType as 'PETROL' | 'DIESEL' },
            { $inc: { availableLiters: amount } },
            { upsert: true, new: true, setDefaultsOnInsert: true }
        );
    }
}
