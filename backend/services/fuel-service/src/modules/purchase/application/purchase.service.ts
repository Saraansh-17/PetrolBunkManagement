import type { IPurchaseRepository } from "../domain/interfaces/purchase-repository.interface.js";
import type { CreatePurchaseDTO } from "../domain/dto/create-purchase.dto.js";
import type { IMessageBroker } from "../../consume/domain/interfaces/message-broker.interface.js";

export class PurchaseService {
    constructor(
        private purchaseRepository: IPurchaseRepository,
        private messageBroker?: IMessageBroker
    ) {
        this.purchaseRepository = purchaseRepository;
    }
    async createPurchase(data: CreatePurchaseDTO) {
        const { vendorName, fuelType, quantity, pricePerLiter, totalCost } = data;
        if (!vendorName || !fuelType || !quantity || !pricePerLiter || !totalCost) {
            throw new Error("All fields are required")
        }
        const purchase = await this.purchaseRepository.save(data);
        
        if (this.messageBroker) {
            await this.messageBroker.publish('fuel.events', {
                eventType: 'FUEL_PURCHASED',
                payload: {
                    fuelType: purchase.fuelType,
                    quantity: purchase.quantity,
                    timestamp: new Date().toISOString()
                }
            });
        }
        
        return purchase;

    }

    async getPurchase(id:string){
        const purchase = await this.purchaseRepository.findById(id)
        return purchase;
    }

    async getAllPurchases(){
        const purchases = await this.purchaseRepository.findAll()
        return purchases;
    }
}