import { Sale } from '../../domain/entities/sale.entity.js';
import type { SaleProps } from '../../domain/entities/sale.entity.js';
import type { ISaleRepository } from '../../domain/interfaces/sale-repository.interface.js';
import type { SaleRequestDTO } from '../../presentation/validation/sale.validation.js';
import type { IMessageBroker } from '../../domain/interfaces/message-broker.interface.js';

export class RecordSaleUseCase {
  constructor(
    private saleRepository: ISaleRepository,
    private messageBroker?: IMessageBroker
  ) {}

  async execute(dto: SaleRequestDTO): Promise<Sale> {
    const saleProps: SaleProps = {
      ...dto,
      timestamp: dto.timestamp ? new Date(dto.timestamp) : new Date()
    };

    const sale = Sale.create(saleProps);
    // We removed synchronous Inventory Service call. We now publish an event asynchronously.
    const savedSale = await this.saleRepository.save(sale);
    
    if (this.messageBroker) {
      await this.messageBroker.publish('fuel.events', {
        eventType: 'FUEL_SOLD',
        payload: {
          fuelType: savedSale.fuelType,
          litersSold: savedSale.litersSold,
          timestamp: savedSale.timestamp
        }
      });
    }
    
    return savedSale;
  }
}
