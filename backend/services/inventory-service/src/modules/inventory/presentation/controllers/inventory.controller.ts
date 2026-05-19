import { Request, Response } from 'express';
import { InventoryService } from '../../application/inventory.service.js';

export class InventoryController {
    constructor(private inventoryService: InventoryService) {}

    getStock = async (req: Request, res: Response) => {
        try {
            // Defaulting to PETROL if not specified or reading from query
            const fuelType = (req.query.fuelType as string) || 'PETROL'; 
            
            if (fuelType !== 'PETROL' && fuelType !== 'DIESEL') {
                return res.status(400).json({
                    timestamp: new Date().toISOString(),
                    status: 400,
                    error: 'Bad Request',
                    message: 'Invalid fuel type provided',
                    path: req.path
                });
            }

            const stockInfo = await this.inventoryService.getStock(fuelType);
            res.status(200).json(stockInfo);
        } catch (error: any) {
            res.status(500).json({
                timestamp: new Date().toISOString(),
                status: 500,
                error: 'Internal Server Error',
                message: error.message,
                path: req.path
            });
        }
    }

    reserveStock = async (req: Request, res: Response) => {
        try {
            const { fuelType, quantity, referenceId } = req.body;

            if (!fuelType || !quantity || !referenceId) {
                return res.status(400).json({
                    timestamp: new Date().toISOString(),
                    status: 400,
                    error: 'Bad Request',
                    message: 'Validation failed: Missing fields',
                    path: req.path
                });
            }

            const result = await this.inventoryService.reserveStock(fuelType, quantity, referenceId);
            res.status(200).json(result);
        } catch (error: any) {
            if (error.message.includes('Insufficient stock')) {
                return res.status(409).json({
                    timestamp: new Date().toISOString(),
                    status: 409,
                    error: 'Conflict',
                    message: 'Unable to reserve fuel stock',
                    path: req.path
                });
            }

            res.status(500).json({
                timestamp: new Date().toISOString(),
                status: 500,
                error: 'Internal Server Error',
                message: error.message,
                path: req.path
            });
        }
    }
}
