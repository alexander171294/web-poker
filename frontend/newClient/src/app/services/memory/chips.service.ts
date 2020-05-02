import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class ChipsService {

  public quantity = 0;
  public sb: number;
  public bb: number;

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

  public setSmallBlind(sb: number) {
    this.sb = sb;
  }

  public getSmallBlind() {
    return this.sb;
  }

  public setBigBlind(bb: number) {
    this.bb = bb;
  }

  public getBigBlind() {
    return this.bb;
  }
}
