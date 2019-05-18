import { Injectable } from '@angular/core';
import { SessionInformation } from './public/SessionInformation';

@Injectable({
  providedIn: 'root'
})
export class SessionService {

  private sessionInfo: SessionInformation;

  constructor() { }

  public setSessionInfo(data: SessionInformation) {
    this.sessionInfo = data;
  }

  public getSessionInfo(): SessionInformation {
    return this.sessionInfo;
  }
}
