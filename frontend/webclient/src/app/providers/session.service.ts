import { Injectable, EventEmitter } from '@angular/core';
import { SessionInformation } from './public/SessionInformation';
import { SessionData } from '../utils/jwt/sessionData';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class SessionService {

  private sessionInfo: SessionInformation;
  private loginEvent: EventEmitter<boolean> = new EventEmitter<boolean>();
  private sessionActive: boolean = false; 

  constructor() { }

  public setSessionInfo(data: SessionInformation) {
    this.sessionInfo = data;
    // inicio de sesión
    const sessionData: SessionData = new SessionData();
    const fechaActual = new Date();
    sessionData.IssuedAt = fechaActual.getTime() / 1000;
    sessionData.Issuer = environment.platform;
    sessionData.notBefore = (fechaActual.getTime() / 1000) - 30; // 30 seconds before now.
    sessionData.Secret = data.jwtPasspharse;
    sessionData.Subject = data.sessionID.toString();
    const min = 100000;
    const max = 999999;
    sessionData.TrackingId =  Math.floor(Math.random() * (max - min + 1)) + min;
    const fechaExpiracion = new Date();
    sessionData.Expiration = Math.floor(
      (fechaExpiracion.getTime() + 1 * 60 * 60 * 1000) / 1000
    );
    // guardamos la sesión:
    sessionData.saveData(environment.sesStorageKey);
    this.sessionActive = true;
    this.loginEvent.emit(true);
  }

  public getSessionInfo(): SessionInformation {
    return this.sessionInfo;
  }

  public getLoginEvent(): EventEmitter<boolean> {
    return this.loginEvent;
  }

  public isSessionActive() {
    return this.sessionActive;
  }
}
