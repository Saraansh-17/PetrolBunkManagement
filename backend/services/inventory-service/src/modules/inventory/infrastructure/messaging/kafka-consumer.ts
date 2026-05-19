import { Kafka } from 'kafkajs';
import type { Consumer } from 'kafkajs';
import { InventoryService } from '../../application/inventory.service.js';
import 'dotenv/config';

export class KafkaConsumerService {
    private kafka: Kafka;
    private consumer: Consumer;
    private isConnected: boolean = false;

    constructor(private inventoryService: InventoryService) {
        const brokers = process.env.KAFKA_BROKERS ? process.env.KAFKA_BROKERS.split(',') : ['localhost:9092'];
        this.kafka = new Kafka({
            clientId: 'inventory-service',
            brokers
        });
        this.consumer = this.kafka.consumer({ groupId: 'inventory-group' });
    }

    async connectAndSubscribe(): Promise<void> {
        try {
            await this.consumer.connect();
            this.isConnected = true;
            console.log('Kafka Consumer connected successfully (Inventory Service)');

            await this.consumer.subscribe({ topic: 'fuel.events', fromBeginning: false });

            await this.consumer.run({
                eachMessage: async ({ topic, partition, message }) => {
                    if (!message.value) return;
                    
                    try {
                        const event = JSON.parse(message.value.toString());
                        console.log(`Received event: ${event.eventType} from topic: ${topic}`);

                        if (event.eventType === 'FUEL_SOLD') {
                            const { fuelType, litersSold } = event.payload;
                            await this.inventoryService.updateStock(fuelType, -litersSold);
                            console.log(`Decremented ${litersSold}L from ${fuelType}`);
                        } else if (event.eventType === 'FUEL_PURCHASED') {
                            const { fuelType, quantity } = event.payload;
                            await this.inventoryService.updateStock(fuelType, quantity);
                            console.log(`Incremented ${quantity}L to ${fuelType}`);
                        }
                    } catch (err) {
                        console.error('Error processing Kafka message:', err);
                    }
                },
            });
        } catch (error) {
            console.warn('Kafka Connection Warning (Inventory Service):', (error as Error).message);
            console.warn('Running without Kafka. Background stock updates will be disabled.');
        }
    }

    async disconnect(): Promise<void> {
        if (this.isConnected) {
            await this.consumer.disconnect();
        }
    }
}
