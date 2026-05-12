import type { Request, Response } from 'express';
import { RecordSaleUseCase } from '../../application/use-cases/record-sale.use-case.js';
import { ListSalesUseCase } from '../../application/use-cases/list-sales.use-case.js';
import { GetSaleDetailsUseCase } from '../../application/use-cases/get-sale-details.use-case.js';
import { saleSchema } from '../validation/sale.validation.js';
import type { SaleRequestDTO } from '../validation/sale.validation.js';

export class SaleController {
  constructor(
    private recordSaleUseCase: RecordSaleUseCase,
    private listSalesUseCase: ListSalesUseCase,
    private getSaleDetailsUseCase: GetSaleDetailsUseCase
  ) {}

  async recordSale(req: Request, res: Response) {
    try {
      const validatedData = saleSchema.parse(req.body);
      const sale = await this.recordSaleUseCase.execute(validatedData);
      
      return res.status(201).json({
        saleId: sale.id || "TEMP_ID",
        status: "SUCCESS",
        message: "Fuel sale recorded successfully",
        inventoryUpdated: true // Mocked for now
      });
    } catch (error: any) {
      if (error.name === 'ZodError') {
        return res.status(400).json({
          status: 400,
          error: "Bad Request",
          message: error.errors[0].message,
          path: "/fuel/sale"
        });
      }
      return res.status(500).json({
        status: 500,
        error: "Internal Server Error",
        message: error.message
      });
    }
  }

  async listSales(req: Request, res: Response) {
    try {
      const page = parseInt(req.query.page as string) || 0;
      const size = parseInt(req.query.size as string) || 10;

      const result = await this.listSalesUseCase.execute(page, size);

      const content = result.content.map(sale => ({
        saleId: sale.id,
        pumpId: sale.pumpId,
        fuelType: sale.fuelType,
        litersSold: sale.litersSold
      }));

      return res.status(200).json({
        content,
        page,
        size
      });
    } catch (error: any) {
      return res.status(500).json({
        status: 500,
        error: "Internal Server Error",
        message: error.message
      });
    }
  }

  async getSaleDetails(req: Request, res: Response) {
    try {
      const id = req.params.id as string;
      const sale = await this.getSaleDetailsUseCase.execute(id);

      if (!sale) {
        return res.status(404).json({
          timestamp: new Date().toISOString(),
          status: 404,
          error: "Not Found",
          message: "Fuel sale record not found",
          path: `/fuel/sales/${id}`
        });
      }

      return res.status(200).json({
        saleId: sale.id,
        pumpId: sale.pumpId,
        fuelType: sale.fuelType,
        litersSold: sale.litersSold,
        totalAmount: sale.totalAmount
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
