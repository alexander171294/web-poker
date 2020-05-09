import { Security } from './Security';
import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { RoomsResponse, RoomResponse } from './roomsResponse';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { ChallengeResponse } from './ChallengeResponse';
import { map } from 'rxjs/internal/operators/map';

@Injectable({
  providedIn: 'root'
})
export class LobbyService {

  public rooms: RoomResponse[];

  constructor(private httpClient: HttpClient) { }

  public getRooms(): Observable<RoomsResponse> {
    const url = environment.apiServer + 'lobby/rooms';
    return this.httpClient.get<RoomsResponse>(url, {headers: Security.getHttpOptionsJWT() })
    .pipe(
      map((x: RoomsResponse) => {
        this.rooms = x.rooms;
        return x;
      })
    );
  }

  public getRoomInfo(roomID: number): RoomResponse{
    let result: RoomResponse;
    if(roomID != null){
      console.log(this.rooms);
      this.rooms.forEach((room) => {
        if(roomID == room.id_room){
          result = room
        }
      });  
    } 
    return result;
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
