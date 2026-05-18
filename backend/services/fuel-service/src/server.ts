import app from "./app.js";
import dotenv from "dotenv";

dotenv.config();
const port = Number(process.env.PORT ?? 3001);

app.listen(port, () => {
  console.log(`Fuel service listening on port ${port}`);
});
