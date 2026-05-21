import "dotenv/config";
import app from "./app.js";
import { connectDB } from "./config/db.js";

const port = Number(process.env.PORT ?? 3001);

const startServer = async () => {
  try {
    await connectDB();
    app.listen(port, () => {
      console.log(`Fuel service listening on port ${port}`);
    });
  } catch (error) {
    console.error("Server startup failed:", error);
    process.exit(1);
  }
};

startServer();
