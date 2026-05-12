import type { ISaleRepository } from '../../domain/interfaces/sale-repository.interface.js';
import type { Sale } from '../../domain/entities/sale.entity.js';

export class ListSalesUseCase {
  constructor(private saleRepository: ISaleRepository) {}

  async execute(page: number, size: number): Promise<{ content: Sale[], total: number }> {
    return await this.saleRepository.findAll(page, size);
  }
}
