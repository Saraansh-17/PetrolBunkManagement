import express from "express";
import cors from "cors";
import helmet from "helmet";
import inventoryRoutes from "./modules/inventory/presentation/routes/inventory.routes.js";

const app = express();

app.use(helmet());
app.use(
  cors({
    origin: process.env.CORS_ORIGINS?.split(",") ?? ["http://localhost:5173"],
    credentials: true,
  })
);
app.use(express.json());

app.get("/inventory/health", (_req, res) => {
  res.status(200).json({ status: "UP", service: "inventory-service" });
});

app.use("/api/v1/inventory", inventoryRoutes);

app.use(
  (
    err: Error,
    req: express.Request,
    res: express.Response,
    _next: express.NextFunction
  ) => {
    console.error(err.stack);
    res.status(500).json({
      timestamp: new Date().toISOString(),
      status: 500,
      error: "Internal Server Error",
      message: err.message || "Something went wrong",
      path: req.path,
    });
  }
);

export default app;
