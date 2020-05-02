import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class ChipsService {

  public quantity = 0;

  constructor() { }

  public substract(quantity: number) {
    this.quantity -= quantity;
  }

  public add(quantity: number) {
    this.quantity += quantity;
  }

  public set(quantity: number) {
    this.quantity = quantity;
  }

  public get(): number {
    return this.quantity;
  }
}
