import type { Request, Response } from "express";
import { PurchaseService } from "../../application/purchase.service.js";
import { purchaseSchema } from "../validation/purchase.validation.js";

export class PurchaseController {
    constructor(private purchaseService: PurchaseService) {}

    async createPurchase(req: Request, res: Response) {
        try {
            const validatedData = purchaseSchema.parse(req.body);
            const purchase = await this.purchaseService.createPurchase(validatedData);

            return res.status(201).json({
                purchaseId: (purchase as any)._id || (purchase as any).id,
                status: "SUCCESS",
                message: "Fuel purchase recorded successfully"
            });
        } catch (error: any) {
            if (error.name === 'ZodError') {
                return res.status(400).json({
                    status: 400,
                    error: "Bad Request",
                    message: error.errors?.[0]?.message,
                    path: "/fuel/purchase/create"
                });
            }
            return res.status(500).json({
                status: 500,
                error: "Internal Server Error",
                message: error.message
            });
        }
    }

    async getPurchase(req: {params:{id:string}}, res: Response) {
        try {
            const id = req.params.id
            const purchase = await this.purchaseService.getPurchase(id);

            if (!purchase) {
                return res.status(404).json({
                    status: 404,
                    error: "Not Found",
                    message: "Fuel purchase record not found",
                    path: `/fuel/purchases/${id}`
                });
            }

            return res.status(200).json({
                purchaseId: (purchase as any)._id || (purchase as any).id,
                vendorName: (purchase as any).vendorName,
                fuelType: (purchase as any).fuelType,
                quantity: (purchase as any).quantity,
                totalCost: (purchase as any).totalCost
            });
        } catch (error: any) {
            return res.status(500).json({
                status: 500,
                error: "Internal Server Error",
                message: error.message
            });
        }
    }

    async getAllPurchases(req: Request, res: Response) {
        try {
            const purchases = await this.purchaseService.getAllPurchases();
            const content = purchases.map((purchase: any) => ({
                purchaseId: purchase._id || purchase.id,
                vendorName: purchase.vendorName,
                fuelType: purchase.fuelType,
                quantity: purchase.quantity,
                totalCost: purchase.totalCost,
                timestamp: purchase.createdAt
            }));

            return res.status(200).json({
                content,
                status: "SUCCESS"
            });
        } catch (error: any) {
            return res.status(500).json({
                status: 500,
                error: "Internal Server Error",
                message: error.message
            });
        }
    }
}