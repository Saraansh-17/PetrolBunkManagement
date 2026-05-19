export interface IMessageBroker {
    connect(): Promise<void>;
    publish(topic: string, message: any): Promise<void>;
    disconnect(): Promise<void>;
}
