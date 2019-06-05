import { Injectable } from '@angular/core';
import { jwtPacker } from 'src/app/utils/jwt/headers';
import { HttpClient } from '@angular/common/http';
import { environment } from 'src/environments/environment';
import { Observable } from 'rxjs';
import { RoomsResponse } from './roomsResponse';

@Injectable({
  providedIn: 'root'
})
export class LobbyService {

  private jwt: jwtPacker = new jwtPacker();

  constructor(private httpClient: HttpClient) { }

  public getRooms(): Observable<RoomsResponse> {
    const url = environment.apiServer + 'lobby/rooms';
    return this.httpClient.get<RoomsResponse>(url, {headers: this.jwt.getHttpOptionsJWT(environment.sesStorageKey) });
  }
}
