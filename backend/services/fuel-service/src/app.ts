import express from "express";
import cors from "cors"
import saleRoutes from "./modules/consume/presentation/routes/sale.routes.js";
import purchaseRoutes from "./modules/purchase/presentation/routes/purchase.routes.js";

const app = express();


app.use(express.json());
app.use(cors())

app.get("/health", (req, res) => {
    res.status(200).json({ status: "Fuel Service is UP" });
  });

app.use("/api/v1/fuel/sales", saleRoutes);
app.use("/api/v1/fuel/purchase", purchaseRoutes);

export default app;