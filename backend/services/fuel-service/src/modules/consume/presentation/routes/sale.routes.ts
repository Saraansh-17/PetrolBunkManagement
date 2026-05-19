import { Router } from 'express';
import { SaleController } from '../controllers/sale.controller.js';
import { RecordSaleUseCase } from '../../application/use-cases/record-sale.use-case.js';
import { ListSalesUseCase } from '../../application/use-cases/list-sales.use-case.js';
import { GetSaleDetailsUseCase } from '../../application/use-cases/get-sale-details.use-case.js';
import { MongooseSaleRepository } from '../../infrastructure/persistence/mongoose/mongoose-sale.repository.js';
import { KafkaMessageBroker } from '../../infrastructure/messaging/kafka-message.broker.js';

const router = Router();

const saleRepository = new MongooseSaleRepository();
const messageBroker = new KafkaMessageBroker();
messageBroker.connect(); // Starts connecting asynchronously

const recordSaleUseCase = new RecordSaleUseCase(saleRepository, messageBroker);
const listSalesUseCase = new ListSalesUseCase(saleRepository);
const getSaleDetailsUseCase = new GetSaleDetailsUseCase(saleRepository);

const saleController = new SaleController(
  recordSaleUseCase,
  listSalesUseCase,
  getSaleDetailsUseCase
);

router.post('/sale', (req, res) => saleController.recordSale(req, res));
router.get('/sales', (req, res) => saleController.listSales(req, res));
router.get('/sales/:id', (req, res) => saleController.getSaleDetails(req, res));

export default router;
