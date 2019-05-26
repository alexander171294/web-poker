import { Injectable } from '@angular/core';
import { UserProfile } from './userProfile';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { environment } from 'src/environments/environment';
import { jwtPacker } from 'src/app/utils/jwt/headers';

@Injectable({
  providedIn: 'root'
})
export class UsersService {

  private jwt: jwtPacker = new jwtPacker();

  constructor(private httpClient: HttpClient) { }

  //httpPacker.getHttpOptionsJWT("loginJWTData")
  public getProfile(id: number): Observable<UserProfile> {
    const url = environment.apiServer+'users/profile/'+id;
    return this.httpClient.get<UserProfile>(url, {headers: this.jwt.getHttpOptionsJWT(environment.sesStorageKey) });
  }

}
