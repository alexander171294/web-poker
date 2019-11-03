import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { RoomsResponse } from './roomsResponse';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class LobbyService {

  constructor(private httpClient: HttpClient) { }

  public getRooms(): Observable<RoomsResponse> {
    const url = environment.apiServer + 'lobby/rooms';
    return this.httpClient.get<RoomsResponse>(url, {headers: this.getHttpOptionsJWT() });
  }

  private getHttpOptionsJWT(): HttpHeaders{
    const identity = localStorage.getItem('sessID');
    const jwt = localStorage.getItem('jwt');
    return new HttpHeaders({ 'Content-Type': 'application/json', 'Authorization': 'Bearer<' + jwt + '>', 'Identity': identity});
  }
}
