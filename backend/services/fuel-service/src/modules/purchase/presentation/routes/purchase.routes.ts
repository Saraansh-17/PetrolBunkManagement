import { Router } from "express";
import { PurchaseController } from "../controllers/purchase.controller.js";
import { PurchaseService } from "../../application/purchase.service.js";
import { PurchaseRepository } from "../../infrastructure/repositories/purchase.repository.js";
const purchaseRouter = Router();

const purchaseRepository = new PurchaseRepository();
const purchaseService = new PurchaseService(purchaseRepository);
const purchaseController = new PurchaseController(purchaseService);

purchaseRouter.post("/create", (req, res) => purchaseController.createPurchase(req, res));
purchaseRouter.get("/purchases", (req, res) => purchaseController.getAllPurchases(req, res));
purchaseRouter.get("/:id", (req, res) => purchaseController.getPurchase(req, res));

export default purchaseRouter;