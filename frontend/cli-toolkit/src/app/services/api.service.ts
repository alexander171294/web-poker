import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';


@Injectable({
  providedIn: 'root'
})
export class ApiService {

  endpoint: string;

  constructor(private http: HttpClient) { }

  setEndpoint(endpoint: string) {
    this.endpoint = endpoint;
  }

  challenge(roomID, claimToken): Observable<string> {
    return this.http.post<string>(this.endpoint + '/mock/lobby/rooms/' + roomID, claimToken);
  }
}
