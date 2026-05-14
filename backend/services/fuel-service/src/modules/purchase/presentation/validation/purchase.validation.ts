import {z} from "zod";
export const purchaseSchema = z.object({
    vendorName : z.string().min(1, "Vendor name is required"),
    fuelType :z.enum(["PETROL", "DIESEL"]),
    quantity : z.number().positive("Quantity must be greater then 0"),
    pricePerLiter : z.number().positive("Price per liter must be greater than 0"),
    totalCost : z.number().positive("total cost must be greater than 0"),

    
})