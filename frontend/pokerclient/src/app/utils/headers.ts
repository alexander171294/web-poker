import { JWToken } from './jwt';
import { HttpHeaders } from '@angular/common/http';
import { SessionData } from './sessionData';

export const httpOptions = {
  headers: new HttpHeaders({ 'Content-Type': 'plain/text' })
    .set('sessID', sessionStorage.getItem('session') != null ? JSON.parse(sessionStorage.getItem('session')).id : 0)
    .set('sessCSRF', sessionStorage.getItem('session') != null ? JSON.parse(sessionStorage.getItem('session')).csrf : '')
};

export class JwtPacker {
  public getHttpOptionsJWT(storageKey: string): {headers} {
    const token: JWToken = new JWToken();
    const sessionData: SessionData = <SessionData>JSON.parse(sessionStorage.getItem(storageKey));

    token.payload = {
      'iss': sessionData.Issuer,
      'sub': sessionData.Subject,
      'exp': sessionData.Expiration,
      'nbf': sessionData.notBefore,
      'iat': sessionData.IssuedAt,
      'jti': sessionData.TrackingId
    };

    console.log('Secret USED: ' + sessionData.Secret);
    const header = new HttpHeaders({
        'Content-Type': 'application/json',
        'Authorization': token.pack(sessionData.Secret),
        'Identity': sessionData.Subject.toString()
    });
    return {
        headers: header
    };
  }

  public getHttpOptions(): {headers} {
    return {
      headers: new HttpHeaders({ 'Content-Type': 'application/json' })
    };
  }
}

export const httpPacker = new JwtPacker();
