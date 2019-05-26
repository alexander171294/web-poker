import { Injectable, EventEmitter } from '@angular/core';
import { SessionInformation } from './public/SessionInformation';

@Injectable({
  providedIn: 'root'
})
export class SessionService {

  private sessionInfo: SessionInformation;
  private loginEvent: EventEmitter<boolean> = new EventEmitter<boolean>();

  constructor() { }

  public setSessionInfo(data: SessionInformation) {
    this.sessionInfo = data;
    this.loginEvent.emit(true);
  }

  public getSessionInfo(): SessionInformation {
    return this.sessionInfo;
  }

  public getLoginEvent(): EventEmitter<boolean> {
    return this.loginEvent;
  }
}
