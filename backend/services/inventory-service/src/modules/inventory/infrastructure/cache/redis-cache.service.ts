import { createClient, RedisClientType } from 'redis';
import { ICacheService } from '../../domain/interfaces/cache-service.interface.js';
import 'dotenv/config';

export class RedisCacheService implements ICacheService {
    private client: RedisClientType;
    private isConnected: boolean = false;

    constructor() {
        const url = process.env.REDIS_URL || 'redis://localhost:6379';
        this.client = createClient({ url });
        
        this.client.on('error', (err) => console.error('Redis Client Error', err));
        this.client.on('connect', () => {
            this.isConnected = true;
            console.log('Redis connected successfully');
        });
        
        this.client.connect().catch(console.error);
    }

    async get(key: string): Promise<string | null> {
        if (!this.isConnected) return null;
        try {
            return await this.client.get(key);
        } catch (error) {
            console.error(`Redis get error for key ${key}:`, error);
            return null;
        }
    }

    async set(key: string, value: string, ttlSeconds: number = 3600): Promise<void> {
        if (!this.isConnected) return;
        try {
            await this.client.setEx(key, ttlSeconds, value);
        } catch (error) {
            console.error(`Redis set error for key ${key}:`, error);
        }
    }

    async del(key: string): Promise<void> {
        if (!this.isConnected) return;
        try {
            await this.client.del(key);
        } catch (error) {
            console.error(`Redis del error for key ${key}:`, error);
        }
    }
}
