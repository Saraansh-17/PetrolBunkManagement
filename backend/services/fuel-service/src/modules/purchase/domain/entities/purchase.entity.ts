export interface IPurchase {
    id?: string;
    vendorName: string;
    fuelType: "PETROL" | "DIESEL";
    quantity: number;
    pricePerLitre: number;
    totalCost: number;
}