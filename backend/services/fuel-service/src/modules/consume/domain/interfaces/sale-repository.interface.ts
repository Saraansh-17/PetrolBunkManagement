import type { Sale } from '../entities/sale.entity.js';

export interface ISaleRepository {
  save(sale: Sale): Promise<Sale>;
  findById(id: string): Promise<Sale | null>;
  findAll(): Promise<Sale[]>;
}
