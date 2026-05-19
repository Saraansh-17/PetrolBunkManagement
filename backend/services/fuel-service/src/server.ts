import app from "./app.js";
import dotenv from "dotenv";

dotenv.config();
const port = Number(process.env.PORT ?? 3001);

app.listen(port, () => {
  console.log(`Fuel service listening on port ${port}`);
});
import { connectDB } from "./config/db.js";

dotenv.config();
const Port = process.env.PORT ||3001;

const startServer = async () => {
    try {
        await connectDB();
        app.listen(Port, () => {
            console.log(`Fuel Service is running on port ${Port}`)
        })
    } catch (error) {
        console.error("Server startup failed:", error);
        process.exit(1);
    }
};

startServer();
