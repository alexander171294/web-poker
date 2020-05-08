import { Injectable, EventEmitter } from '@angular/core';
import { UserOpenEventData } from './UserOpenEventData';

@Injectable({
  providedIn: 'root'
})
export class UserMenuControllerService {

  private userMenuEvent: EventEmitter<UserOpenEventData | boolean> = new EventEmitter<UserOpenEventData | boolean>();

  constructor() { }

  public show(x: number, y: number, uid: number) {
    this.userMenuEvent.emit(new UserOpenEventData(x, y, uid));
  }

  public hide() {
    this.userMenuEvent.emit(false);
  }

  public getUME(): EventEmitter<UserOpenEventData | boolean> {
    return this.userMenuEvent;
  }
}
