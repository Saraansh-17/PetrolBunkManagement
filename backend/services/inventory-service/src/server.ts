import "dotenv/config";
import app from "./app.js";
import { connectDB } from "./config/db.js";

const port = Number(process.env.PORT ?? 3002);

const startServer = async () => {
  try {
    await connectDB();
    app.listen(port, () => {
      console.log(`Inventory service listening on port ${port}`);
    });
  } catch (error) {
    console.error("Failed to start server:", error);
    process.exit(1);
  }
};

startServer();
