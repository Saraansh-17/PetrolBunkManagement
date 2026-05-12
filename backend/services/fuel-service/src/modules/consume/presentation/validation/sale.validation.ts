import { z } from "zod";

export const saleSchema = z.object({
  pumpId: z.string().min(1, "Pump ID is required"),
  employeeId: z.string().min(1, "Employee ID is required"),
  fuelType: z.enum(["PETROL", "DIESEL"]),
  litersSold: z.number().positive("Liters sold must be greater than 0"),
  pricePerLiter: z.number().positive("Price per liter must be greater than 0"),
  totalAmount: z.number().positive("Total amount must be greater than 0"),
  paymentMode: z.enum(["CASH", "CARD", "UPI"]),
  timestamp: z.string().optional() // API contract says it's a string in ISO-8601
});

export type SaleRequestDTO = z.infer<typeof saleSchema>;
