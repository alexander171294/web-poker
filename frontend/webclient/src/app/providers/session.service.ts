import { Injectable, EventEmitter } from '@angular/core';
import { SessionInformation } from './public/SessionInformation';
import { SessionData } from '../utils/jwt/sessionData';
import { environment } from 'src/environments/environment.prod';

@Injectable({
  providedIn: 'root'
})
export class SessionService {

  private sessionInfo: SessionInformation;
  private loginEvent: EventEmitter<boolean> = new EventEmitter<boolean>();

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
    let min = 100000;
    let max = 999999;
    sessionData.TrackingId =  Math.floor(Math.random() * (max - min + 1)) + min;  
    let fechaExpiracion = new Date();
    sessionData.Expiration = Math.floor(
      (fechaExpiracion.getTime() + 1 * 60 * 60 * 1000) / 1000
    );
    // guardamos la sesión:
    sessionData.saveData("JWTData");
    this.loginEvent.emit(true);
  }

  public getSessionInfo(): SessionInformation {
    return this.sessionInfo;
  }

  public getLoginEvent(): EventEmitter<boolean> {
    return this.loginEvent;
  }
}
