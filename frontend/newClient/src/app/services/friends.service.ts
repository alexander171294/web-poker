import { environment } from 'src/environments/environment';
import { HttpClient } from '@angular/common/http';
import { UserProfile } from './UserProfile';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Security } from './Security';

@Injectable({
  providedIn: 'root'
})
export class FriendsService {

  constructor(private httpC: HttpClient) { }

  public getFriends(id: string): Observable<any> {
    return this.httpC.get<UserProfile>(environment.apiServer + 'friends/' + id, { headers: Security.getHttpOptionsJWT() });
  }
}
