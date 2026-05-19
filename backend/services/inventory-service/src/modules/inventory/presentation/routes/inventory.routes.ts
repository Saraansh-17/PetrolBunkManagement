import { Router } from 'express';
import { InventoryController } from '../controllers/inventory.controller.js';
import { InventoryService } from '../../application/inventory.service.js';
import { MongooseInventoryRepository } from '../../infrastructure/persistence/mongoose/mongoose-inventory.repository.js';
import { RedisCacheService } from '../../infrastructure/cache/redis-cache.service.js';
import { KafkaConsumerService } from '../../infrastructure/messaging/kafka-consumer.js';

const router = Router();

// Dependency Injection
const inventoryRepository = new MongooseInventoryRepository();
const cacheService = new RedisCacheService();
const inventoryService = new InventoryService(inventoryRepository, cacheService);

// Start Kafka Consumer
const kafkaConsumer = new KafkaConsumerService(inventoryService);
kafkaConsumer.connectAndSubscribe();

const inventoryController = new InventoryController(inventoryService);

router.get('/stock', inventoryController.getStock);
router.post('/reserve', inventoryController.reserveStock);

export default router;
