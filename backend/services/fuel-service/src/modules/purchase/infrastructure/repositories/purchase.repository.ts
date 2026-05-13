import type { IPurchaseRepository } from "../../domain/interfaces/purchase-repository.interface.js";
import {Purchase} from "../schemas/purchase.schema.js"

 export class PurchaseRepository implements IPurchaseRepository{
    async save(data:any){
        return await Purchase.create(data)
    }
    async findById(id:string){
        return await Purchase.findById(id)
    }
    async findAll(){
        return await Purchase.find()
    }
}