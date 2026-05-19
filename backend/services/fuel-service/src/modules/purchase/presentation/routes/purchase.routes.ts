import { Router } from "express";
import { PurchaseController } from "../controllers/purchase.controller.js";
import { PurchaseService } from "../../application/purchase.service.js";
import { PurchaseRepository } from "../../infrastructure/repositories/purchase.repository.js";
import { KafkaMessageBroker } from "../../../consume/infrastructure/messaging/kafka-message.broker.js";

const purchaseRouter = Router();

const purchaseRepository = new PurchaseRepository();
const messageBroker = new KafkaMessageBroker();
messageBroker.connect();

const purchaseService = new PurchaseService(purchaseRepository, messageBroker);
const purchaseController = new PurchaseController(purchaseService);

purchaseRouter.post("/create", (req, res) => purchaseController.createPurchase(req, res));
purchaseRouter.get("/purchases", (req, res) => purchaseController.getAllPurchases(req, res));
purchaseRouter.get("/:id", (req, res) => purchaseController.getPurchase(req, res));

export default purchaseRouter;