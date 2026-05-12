import type { ISaleRepository } from '../../domain/interfaces/sale-repository.interface.js';
import type { Sale } from '../../domain/entities/sale.entity.js';

export class GetSaleDetailsUseCase {
  constructor(private saleRepository: ISaleRepository) {}

  async execute(id: string): Promise<Sale | null> {
    return await this.saleRepository.findById(id);
  }
}
