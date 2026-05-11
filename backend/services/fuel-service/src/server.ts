import app from "./app.js";
import dotenv from "dotenv";
// import { connectDB } from "./config/db.js";

dotenv.config();
const Port = process.env.PORT || 5000;

const startServer = async () => {
    try {
        
        console.log(" Database Connected");

        app.listen(Port, () => {
            console.log(`Fuel Service is running on port ${Port}`)
        })
    } catch (error) {
        console.error("Server startup failed:", error);
        process.exit(1);
    }
};

startServer();