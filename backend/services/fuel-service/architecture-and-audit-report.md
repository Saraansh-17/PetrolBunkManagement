# Fuel Service: Architecture & Audit Report

## Overview
This report details the architectural state and audit findings for the `fuel-service`. The service has been fully modernized to utilize an **Event-Driven Architecture** via Apache Kafka, entirely decoupling it from the Inventory Service.

## Audit Findings

> [!TIP]
> **Asynchronous Event-Driven Architecture**
> The `fuel-service` now acts strictly as a **Kafka Producer**. It no longer relies on synchronous API calls to the `inventory-service` to deduct or add stock. Instead, it fires events and allows eventual consistency to manage stock levels. This significantly improves the throughput of the fuel station pumps during high load.

> [!WARNING]
> **Testing Coverage Note**
> Automated tests for `fuel-service` (Vitest/Supertest) are currently not configured. Given the architectural focus on the Kafka pipeline in this iteration, establishing a full unit/integration test suite for `fuel-service` remains as technical debt to be addressed in the next sprint.

## Kafka Integration

The service utilizes `kafkajs` to connect to the local Kafka broker (`localhost:9092`). 

### Published Events

#### 1. `FUEL_SOLD`
- **Topic**: `fuel.events`
- **Trigger**: Fired when `RecordSaleUseCase` successfully persists a customer fuel sale to MongoDB.
- **Payload**:
  ```json
  {
    "eventType": "FUEL_SOLD",
    "payload": {
      "fuelType": "PETROL",
      "litersSold": 45.5,
      "timestamp": "2026-05-19T10:30:00.000Z"
    }
  }
  ```

#### 2. `FUEL_PURCHASED`
- **Topic**: `fuel.events`
- **Trigger**: Fired when `PurchaseService` successfully persists a bulk vendor purchase to MongoDB.
- **Payload**:
  ```json
  {
    "eventType": "FUEL_PURCHASED",
    "payload": {
      "fuelType": "DIESEL",
      "quantity": 1000,
      "timestamp": "2026-05-19T10:35:00.000Z"
    }
  }
  ```

> [!NOTE]
> **Graceful Degradation**
> The `KafkaMessageBroker` implementation includes a fail-safe connection block. If the local machine does not have a running Kafka instance, the service will log a warning and continue to operate, allowing developers to test HTTP endpoints without breaking the runtime.

## Next Steps
The `fuel-service` builds perfectly and is ready to be merged. The environment is fully documented via `.env.example`.
