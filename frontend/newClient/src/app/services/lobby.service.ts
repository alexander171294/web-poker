import { Security } from './Security';
import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { RoomsResponse } from './roomsResponse';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { ChallengeResponse } from './ChallengeResponse';

@Injectable({
  providedIn: 'root'
})
export class LobbyService {

  constructor(private httpClient: HttpClient) { }

  public getRooms(): Observable<RoomsResponse> {
    const url = environment.apiServer + 'lobby/rooms';
    return this.httpClient.get<RoomsResponse>(url, {headers: Security.getHttpOptionsJWT() });
  }

  challenge(roomID, claimToken): Observable<ChallengeResponse> {
    const url = environment.apiServer + 'lobby/rooms/' + roomID;
    return this.httpClient.post<ChallengeResponse>(url, claimToken, {headers: Security.getHttpOptionsJWT() });
  }

  challengeD(roomID, claimToken, chips): Observable<ChallengeResponse> {
    const url = environment.apiServer + 'lobby/rooms/' + roomID + '?deposit=' + chips;
    return this.httpClient.post<ChallengeResponse>(url, claimToken, {headers: Security.getHttpOptionsJWT() });
  }
}
