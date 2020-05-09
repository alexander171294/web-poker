import { environment } from 'src/environments/environment';
import { HttpClient } from '@angular/common/http';
import { UserProfile, FriendCard, FriendData } from './UserProfile';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Security } from './Security';
import { GeneralResponse } from './GeneralResponse';
import { FriendshipStatus } from './FriendshipStatus';

@Injectable({
  providedIn: 'root'
})
export class FriendsService {

  constructor(private httpC: HttpClient) { }

  public getFriends(): Observable<FriendData> {
    return this.httpC.get<FriendData>(environment.apiServer + 'friends', { headers: Security.getHttpOptionsJWT() });
  }

  public sendFriendsRequest(fid: number): Observable<GeneralResponse> {
    return this.httpC.post<GeneralResponse>(environment.apiServer + 'friends/requests/' + fid, {}, { headers: Security.getHttpOptionsJWT() });
  }

  public getFriendshipStatus(target: number): Observable<FriendshipStatus> {
    return this.httpC.get<FriendshipStatus>(environment.apiServer + 'friends/' + target, { headers: Security.getHttpOptionsJWT() });
  }

  public removeFriendship(fid: number): Observable<GeneralResponse> {
    return this.httpC.delete<GeneralResponse>(environment.apiServer + 'friends/' + fid, { headers: Security.getHttpOptionsJWT() });
  }
}
