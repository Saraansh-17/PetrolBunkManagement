import app from "./app.js";
import dotenv from "dotenv";

dotenv.config();
const port = Number(process.env.PORT ?? 3002);

app.listen(port, () => {
  console.log(`Inventory service listening on port ${port}`);
});
import 'dotenv/config';
import { connectDB } from "./config/db.js";
const PORT = process.env.PORT || 3002;

const startServer = async () => {
    try {
        await connectDB();
        app.listen(PORT, () => {
            console.log(`Inventory Service is running on port ${PORT}`);
        });
    } catch (error) {
        console.error("Failed to start server:", error);
        process.exit(1);
    }
};

startServer();
