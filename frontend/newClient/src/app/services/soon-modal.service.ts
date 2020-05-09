import { Injectable, EventEmitter } from '@angular/core';
import { Room } from './roomsResponse';

@Injectable({
  providedIn: 'root'
})

export class SoonModalService {
  
  private soonModalEvent: EventEmitter<boolean> = new EventEmitter<boolean>();

  constructor() { }

  public show() {
    this.soonModalEvent.emit(true);
  }

  public hide() {
    this.soonModalEvent.emit(false);
  }

  public getsME(): EventEmitter<boolean> {
    return this.soonModalEvent;
  }
}

