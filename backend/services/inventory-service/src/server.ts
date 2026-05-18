import app from "./app.js";
import dotenv from "dotenv";

dotenv.config();
const port = Number(process.env.PORT ?? 3002);

app.listen(port, () => {
  console.log(`Inventory service listening on port ${port}`);
});
