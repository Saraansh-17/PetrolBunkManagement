import express from "express";
import cors from "cors";
import cors from "cors"
import saleRoutes from "./modules/consume/presentation/routes/sale.routes.js";
import purchaseRoutes from "./modules/purchase/presentation/routes/purchase.routes.js";

const app = express();

app.use(express.json());
app.use(
  cors({
    origin: process.env.CORS_ORIGINS?.split(",") ?? ["http://localhost:5173"],
    credentials: true,
  })
);

app.get("/fuel/health", (_req, res) => {
  res.status(200).json({ status: "UP", service: "fuel-service" });
});

const nozzles = [
  { id: "NZ-01", fuelType: "PETROL", status: "ACTIVE" },
  { id: "NZ-02", fuelType: "DIESEL", status: "ACTIVE" },
];

const sales: Array<Record<string, unknown>> = [];

app.get("/fuel/nozzles", (_req, res) => {
  res.json({ data: nozzles });
});

app.get("/fuel/sales", (_req, res) => {
  res.json({ data: sales });
});

app.post("/fuel/sales", (req, res) => {
  const sale = {
    id: sales.length + 1,
    ...req.body,
    createdAt: new Date().toISOString(),
  };
  sales.push(sale);
  res.status(201).json({ message: "Fuel sale recorded", data: sale });
});
app.use("/api/v1/fuel", saleRoutes);
app.use("/api/v1/fuel/purchase", purchaseRoutes);

export default app;
