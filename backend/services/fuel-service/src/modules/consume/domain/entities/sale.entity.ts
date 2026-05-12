export type FuelType = 'PETROL' | 'DIESEL';
export type PaymentMode = 'CASH' | 'CARD' | 'UPI';

export interface SaleProps {
  id?: string;
  pumpId: string;
  employeeId: string;
  fuelType: FuelType;
  litersSold: number;
  pricePerLiter: number;
  totalAmount: number;
  paymentMode: PaymentMode;
  timestamp: Date;
}

export class Sale {
  private constructor(private props: SaleProps) {}

  public static create(props: SaleProps): Sale {
    if (props.litersSold <= 0) {
      throw new Error('Liters sold must be greater than 0');
    }
    if (props.pricePerLiter <= 0) {
      throw new Error('Price per liter must be greater than 0');
    }
    if (props.totalAmount <= 0) {
      throw new Error('Total amount must be greater than 0');
    }

    return new Sale({
      ...props,
      timestamp: props.timestamp || new Date(),
    });
  }

  public getProps(): SaleProps {
    return { ...this.props };
  }

  get id(): string | undefined { return this.props.id; }
  get pumpId(): string { return this.props.pumpId; }
  get employeeId(): string { return this.props.employeeId; }
  get fuelType(): FuelType { return this.props.fuelType; }
  get litersSold(): number { return this.props.litersSold; }
  get pricePerLiter(): number { return this.props.pricePerLiter; }
  get totalAmount(): number { return this.props.totalAmount; }
  get paymentMode(): PaymentMode { return this.props.paymentMode; }
  get timestamp(): Date { return this.props.timestamp; }
}
