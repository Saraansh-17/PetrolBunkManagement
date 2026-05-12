import { Sale } from '../../domain/entities/sale.entity.js';
import type { SaleProps } from '../../domain/entities/sale.entity.js';
import type { ISaleRepository } from '../../domain/interfaces/sale-repository.interface.js';
import type { SaleRequestDTO } from '../../presentation/validation/sale.validation.js';

export class RecordSaleUseCase {
  constructor(private saleRepository: ISaleRepository) {}

  async execute(dto: SaleRequestDTO): Promise<Sale> {
    const saleProps: SaleProps = {
      ...dto,
      timestamp: dto.timestamp ? new Date(dto.timestamp) : new Date()
    };

    const sale = Sale.create(saleProps);
    
    // Note: In a real scenario, we would also interact with the Inventory Service 
    // to deduct stock here. For now, we focus on recording the sale.
    
    return await this.saleRepository.save(sale);
  }
}
