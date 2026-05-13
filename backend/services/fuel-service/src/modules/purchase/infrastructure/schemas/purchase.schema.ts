import mongoose from "mongoose";
const purchaseSchema = new mongoose.Schema({
    vendorName:{
        type:String,
        required:[true,"Vendor name is required"]
    },
    fuelType:{
        type:String,
        enum:["PETROL","DIESEL"],
        required:[true,"Fuel type is required"]
    },
    quantity:{
        type:Number,
        required:[true,"Quantity is required"]
    },
    pricePerLiter:{
        type:Number,
        required:[true,"Price per liter is required"]
    },
    totalCost:{
        type:Number,
        // required:[true,"Total cost is required"]
    },
   
},{
    timestamps:true
})

export const Purchase = mongoose.model("Purchase",purchaseSchema);