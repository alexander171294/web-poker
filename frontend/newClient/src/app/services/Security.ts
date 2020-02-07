import { HttpHeaders } from '@angular/common/http';

export class Security {

  public static getHttpOptionsJWT(): HttpHeaders{
    const identity = localStorage.getItem('sessID');
    const jwt = localStorage.getItem('jwt');
    return new HttpHeaders({ 'Content-Type': 'application/json', 'Authorization': 'Bearer<' + jwt + '>', 'Identity': identity});
  }

  public static getJWTData(): JwtPayload {
    return JSON.parse(atob(localStorage.getItem('jwt').split('.')[1]));
  }

}

export class JwtPayload {
  public jti: string;
  public iat: string;
  public sub: string;
  public iss: string;
  public exp: number;
}
