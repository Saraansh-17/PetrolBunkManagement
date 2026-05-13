export interface CreatePurchaseDTO{
    vendorName:string;
    fuelType:"PETROL"|"DIESEL";
    quantity:number;
    pricePerLiter:number;
    totalCost?:number;
}