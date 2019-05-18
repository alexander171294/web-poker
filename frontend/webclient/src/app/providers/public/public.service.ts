import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment.prod';
import { SessionInformation } from './SessionInformation';
import { SignupRequest } from './SignupRequest';
import { LoginRequest } from './LoginRequest';

@Injectable({
  providedIn: 'root'
})
export class PublicService {

  constructor(private httpClient: HttpClient) { }

  public signup(data: SignupRequest): Observable<SessionInformation> {
    const url = environment.apiServer + 'public/signup';
    return this.httpClient.post<SessionInformation>(url, data);
  }

  public login(data: LoginRequest): Observable<SessionInformation> {
    const url = environment.apiServer + 'public/login';
    return this.httpClient.post<SessionInformation>(url, data);
  }

}
