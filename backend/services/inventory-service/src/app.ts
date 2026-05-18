import express from "express";
import cors from "cors";

const app = express();

app.use(express.json());
app.use(
  cors({
    origin: process.env.CORS_ORIGINS?.split(",") ?? ["http://localhost:5173"],
    credentials: true,
  })
);

const items: Array<Record<string, unknown>> = [];

app.get("/inventory/health", (_req, res) => {
  res.status(200).json({ status: "UP", service: "inventory-service" });
});

app.get("/inventory/items", (_req, res) => {
  res.json({ data: items });
});

app.post("/inventory/items", (req, res) => {
  const item = {
    id: items.length + 1,
    ...req.body,
    updatedAt: new Date().toISOString(),
  };
  items.push(item);
  res.status(201).json({ message: "Inventory item created", data: item });
});

export default app;
