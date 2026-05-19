import { Kafka } from 'kafkajs';
import type { Producer } from 'kafkajs';
import type { IMessageBroker } from '../../domain/interfaces/message-broker.interface.js';
import 'dotenv/config';

export class KafkaMessageBroker implements IMessageBroker {
    private kafka: Kafka;
    private producer: Producer;
    private isConnected: boolean = false;

    constructor() {
        const brokers = process.env.KAFKA_BROKERS ? process.env.KAFKA_BROKERS.split(',') : ['localhost:9092'];
        this.kafka = new Kafka({
            clientId: 'fuel-service',
            brokers
        });
        this.producer = this.kafka.producer();
    }

    async connect(): Promise<void> {
        try {
            await this.producer.connect();
            this.isConnected = true;
            console.log('Kafka Producer connected successfully (Fuel Service)');
        } catch (error) {
            console.warn('Kafka Connection Warning (Fuel Service):', (error as Error).message);
            console.warn('Running without Kafka. Events will not be published.');
            // Non-fatal, allowing local dev without Kafka
        }
    }

    async publish(topic: string, message: any): Promise<void> {
        if (!this.isConnected) {
            console.log(`[DEV MODE] Kafka not connected. Would have published to ${topic}:`, message);
            return;
        }
        try {
            await this.producer.send({
                topic,
                messages: [{ value: JSON.stringify(message) }]
            });
            console.log(`Successfully published event to ${topic}`);
        } catch (error) {
            console.error(`Error publishing to ${topic}:`, error);
        }
    }

    async disconnect(): Promise<void> {
        if (this.isConnected) {
            await this.producer.disconnect();
        }
    }
}
