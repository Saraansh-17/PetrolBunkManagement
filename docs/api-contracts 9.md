# Petrol Bunk Management System - API Contracts

## Base URL

```txt
/api/v1
```

---

# Fuel Management APIs

## 1. Record Fuel Sale

### Endpoint

```http
POST /fuel/sale
```

### Description

Record customer fuel purchase and deduct inventory stock atomically.

### Request Body

```json
{
  "pumpId": "PUMP01",
  "employeeId": "EMP201",
  "fuelType": "PETROL",
  "litersSold": 45.5,
  "pricePerLiter": 102.5,
  "totalAmount": 4663.75,
  "paymentMode": "UPI",
  "timestamp": "2026-05-11T10:30:00"
}
```

### Success Response

#### Status Code

```txt
201 CREATED
```

#### Response Body

```json
{
  "saleId": 1,
  "status": "SUCCESS",
  "message": "Fuel sale recorded successfully",
  "inventoryUpdated": true
}
```

### Error Responses

#### 400 BAD REQUEST

```json
{
  "timestamp": "2026-05-11T10:30:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Invalid fuel type provided",
  "path": "/fuel/sale"
}
```

#### 409 CONFLICT

```json
{
  "timestamp": "2026-05-11T10:30:00",
  "status": 409,
  "error": "Conflict",
  "message": "Insufficient PETROL stock available",
  "path": "/fuel/sale"
}
```

---

## 2. Record Fuel Purchase

### Endpoint

```http
POST /fuel/purchase
```

### Description

Record bulk fuel purchase from supplier and update inventory stock.

### Request Body

```json
{
  "vendorName": "Indian Oil",
  "fuelType": "DIESEL",
  "quantity": 1000,
  "pricePerLiter": 89.5,
  "totalCost": 89500
}
```

### Success Response

#### Status Code

```txt
201 CREATED
```

#### Response Body

```json
{
  "purchaseId": 101,
  "status": "SUCCESS",
  "message": "Fuel purchase recorded successfully"
}
```

---

## 3. List Fuel Sales

### Endpoint

```http
GET /fuel/sales?page=0&size=10
```

### Description

Fetch all fuel sales with pagination support.

### Success Response

#### Status Code

```txt
200 OK
```

#### Response Body

```json
{
  "content": [
    {
      "saleId": 1,
      "pumpId": "PUMP01",
      "fuelType": "PETROL",
      "litersSold": 45.5
    }
  ],
  "page": 0,
  "size": 10
}
```

---

## 4. Get Fuel Sale Details

### Endpoint

```http
GET /fuel/sales/{id}
```

### Description

Fetch details of a specific fuel sale.

### Path Variables

| Name | Type |
|------|------|
| id | Long |

### Success Response

#### Status Code

```txt
200 OK
```

#### Response Body

```json
{
  "saleId": 1,
  "pumpId": "PUMP01",
  "fuelType": "PETROL",
  "litersSold": 45.5,
  "totalAmount": 4663.75
}
```

### Error Response

#### 404 NOT FOUND

```json
{
  "timestamp": "2026-05-11T10:30:00",
  "status": 404,
  "error": "Not Found",
  "message": "Fuel sale record not found",
  "path": "/fuel/sales/1"
}
```

---

## 5. List Fuel Purchases

### Endpoint

```http
GET /fuel/purchases
```

### Description

Fetch all fuel purchase records.

### Success Response

#### Status Code

```txt
200 OK
```

#### Response Body

```json
{
  "content": [
    {
      "purchaseId": 101,
      "vendorName": "Indian Oil"
    }
  ]
}
```

---

## 6. Get Fuel Purchase Details

### Endpoint

```http
GET /fuel/purchases/{id}
```

### Description

Fetch details of a specific fuel purchase.

### Success Response

#### Status Code

```txt
200 OK
```

#### Response Body

```json
{
  "purchaseId": 101,
  "vendorName": "Indian Oil",
  "fuelType": "DIESEL",
  "quantity": 1000
}
```

---

# Inventory APIs

## 7. Get Fuel Stock

### Endpoint

```http
GET /inventory/stock
```

### Description

Fetch current fuel stock from Redis cache or database fallback.

### Success Response

#### Status Code

```txt
200 OK
```

#### Response Body

```json
{
  "fuelType": "PETROL",
  "availableLiters": 5400,
  "source": "REDIS"
}
```

---

## 8. Reserve Fuel Stock

### Endpoint

```http
POST /inventory/reserve
```

### Description

Reserve fuel stock atomically before completing transaction.

### Request Body

```json
{
  "fuelType": "PETROL",
  "quantity": 50,
  "referenceId": "SALE1001"
}
```

### Success Response

#### Status Code

```txt
200 OK
```

#### Response Body

```json
{
  "reservationId": "RES101",
  "status": "RESERVED"
}
```

### Error Response

#### 409 CONFLICT

```json
{
  "timestamp": "2026-05-11T10:30:00",
  "status": 409,
  "error": "Conflict",
  "message": "Unable to reserve fuel stock",
  "path": "/inventory/reserve"
}
```

---

# Employee Management APIs

## 9. Get All Employees

### Endpoint

```http
GET /employees
```

### Description

Fetch all petrol bunk employees.

### Success Response

#### Status Code

```txt
200 OK
```

#### Response Body

```json
[
  {
    "employeeId": "EMP201",
    "name": "Rahul Sharma"
  }
]
```

---

## 10. Get Employee Details

### Endpoint

```http
GET /employees/{id}
```

### Description

Fetch employee details.

### Success Response

#### Status Code

```txt
200 OK
```

#### Response Body

```json
{
  "employeeId": "EMP201",
  "name": "Rahul Sharma",
  "department": "Operations",
  "designation": "Pump Operator"
}
```

### Error Response

#### 404 NOT FOUND

```json
{
  "timestamp": "2026-05-11T10:30:00",
  "status": 404,
  "error": "Not Found",
  "message": "Employee not found",
  "path": "/employees/EMP201"
}
```

---

## 11. Create Employee

### Endpoint

```http
POST /employees
```

### Description

Add a new employee to the petrol bunk system.

### Request Body

```json
{
  "name": "Rahul Sharma",
  "department": "Operations",
  "designation": "Pump Operator"
}
```

### Success Response

#### Status Code

```txt
201 CREATED
```

#### Response Body

```json
{
  "employeeId": "EMP201",
  "status": "CREATED"
}
```

---

# Attendance APIs

## 12. Mark Attendance

### Endpoint

```http
POST /attendance
```

### Description

Mark employee attendance.

### Request Body

```json
{
  "employeeId": "EMP201",
  "status": "PRESENT",
  "checkInTime": "2026-05-11T08:30:00"
}
```

### Success Response

#### Status Code

```txt
201 CREATED
```

#### Response Body

```json
{
  "attendanceId": 5001,
  "status": "RECORDED"
}
```

### Error Response

#### 409 CONFLICT

```json
{
  "timestamp": "2026-05-11T10:30:00",
  "status": 409,
  "error": "Conflict",
  "message": "Attendance already marked for today",
  "path": "/attendance"
}
```

---

## 13. List Attendance Records

### Endpoint

```http
GET /attendance
```

### Description

Fetch attendance records.

### Success Response

#### Status Code

```txt
200 OK
```

#### Response Body

```json
{
  "content": [
    {
      "attendanceId": 5001,
      "employeeId": "EMP201"
    }
  ]
}
```

---

## 14. Get Attendance Record

### Endpoint

```http
GET /attendance/{id}
```

### Description

Fetch specific attendance details.

### Success Response

#### Status Code

```txt
200 OK
```

#### Response Body

```json
{
  "attendanceId": 5001,
  "employeeId": "EMP201",
  "status": "PRESENT"
}
```

---

# Standard Error Response

```json
{
  "timestamp": "2026-05-11T12:30:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed",
  "path": "/api/path"
}
```

---

# Validation Rules

| Field | Validation |
|------|------------|
| fuelType | Must be PETROL or DIESEL |
| litersSold | Must be greater than 0 |
| quantity | Must be greater than 0 |
| employeeId | Cannot be null |
| paymentMode | CASH, CARD, or UPI |
| timestamp | Must follow ISO-8601 format |

---

# HTTP Status Codes

| Status Code | Meaning |
|-------------|---------|
| 200 OK | Request successful |
| 201 CREATED | Resource created successfully |
| 400 BAD REQUEST | Invalid request payload |
| 401 UNAUTHORIZED | Authentication required |
| 403 FORBIDDEN | Access denied |
| 404 NOT FOUND | Resource not found |
| 409 CONFLICT | Business rule conflict |
| 500 INTERNAL SERVER ERROR | Unexpected server error |

---

# Headers

```http
Content-Type: application/json
Accept: application/json
```

---

# Notes

- All APIs use JSON request/response format.
- Inventory operations must be atomic.
- Redis caching is used for stock retrieval optimization.
- Inventory service supports DB fallback in case Redis is unavailable.
- Timestamp format follows ISO-8601 standard.
- Pagination is supported for listing APIs.
- All monetary values are in INR.