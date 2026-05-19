import express from 'express';
import cors from 'cors';
import helmet from 'helmet';

const app = express();

app.use(helmet());
app.use(cors());
app.use(express.json());

import inventoryRoutes from './modules/inventory/presentation/routes/inventory.routes.js';

app.use('/api/v1/inventory', inventoryRoutes);

app.use((err: any, req: express.Request, res: express.Response, next: express.NextFunction) => {
  console.error(err.stack);
  res.status(500).json({
    timestamp: new Date().toISOString(),
    status: 500,
    error: 'Internal Server Error',
    message: err.message || 'Something went wrong',
    path: req.path
  });
});

export default app;
