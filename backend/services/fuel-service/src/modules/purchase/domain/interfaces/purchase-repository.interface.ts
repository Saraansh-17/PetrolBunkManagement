export interface IPurchaseRepository{
    save(data:any):Promise<any>
    findById(id:string):Promise<any>
    findAll():Promise<any[]>
    
}