import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ChallengeResponse } from './api/ChallengeResponse';


@Injectable({
  providedIn: 'root'
})
export class ApiService {

  endpoint: string;

  constructor(private http: HttpClient) { }

  setEndpoint(endpoint: string) {
    this.endpoint = endpoint;
  }

  challenge(roomID, claimToken): Observable<ChallengeResponse> {
    return this.http.post<ChallengeResponse>(this.endpoint + '/mock/lobby/rooms/' + roomID, claimToken);
  }
}
