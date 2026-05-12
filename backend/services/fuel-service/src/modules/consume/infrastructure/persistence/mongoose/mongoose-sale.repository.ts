import { Sale } from '../../../domain/entities/sale.entity.js';
import type { FuelType, PaymentMode } from '../../../domain/entities/sale.entity.js';
import type { ISaleRepository } from '../../../domain/interfaces/sale-repository.interface.js';
import { SaleModel } from './sale.model.js';
import type { ISaleDocument } from './sale.model.js';

export class MongooseSaleRepository implements ISaleRepository {
  private toDomain(doc: ISaleDocument): Sale {
    return Sale.create({
      id: doc._id.toString(),
      pumpId: doc.pumpId,
      employeeId: doc.employeeId,
      fuelType: doc.fuelType as FuelType,
      litersSold: doc.litersSold,
      pricePerLiter: doc.pricePerLiter,
      totalAmount: doc.totalAmount,
      paymentMode: doc.paymentMode as PaymentMode,
      timestamp: doc.timestamp
    });
  }

  async save(sale: Sale): Promise<Sale> {
    const props = sale.getProps();
    const doc = await SaleModel.create(props);
    return this.toDomain(doc);
  }

  async findById(id: string): Promise<Sale | null> {
    const doc = await SaleModel.findById(id);
    return doc ? this.toDomain(doc) : null;
  }

  async findAll(page: number, size: number): Promise<{ content: Sale[], total: number }> {
    const total = await SaleModel.countDocuments();
    const docs = await SaleModel.find()
      .skip(page * size)
      .limit(size)
      .sort({ timestamp: -1 });

    return {
      content: docs.map(doc => this.toDomain(doc)),
      total
    };
  }
}
