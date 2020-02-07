import { environment } from 'src/environments/environment';
import { HttpClient } from '@angular/common/http';
import { UserProfile } from './UserProfile';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Security } from './Security';

@Injectable({
  providedIn: 'root'
})
export class UsersService {

  constructor(private httpC: HttpClient) { }

  public getUser(id: string): Observable<UserProfile> {
    return this.httpC.get<UserProfile>(environment.apiServer + 'users/profile/' + id, { headers: Security.getHttpOptionsJWT() });
  }
}
