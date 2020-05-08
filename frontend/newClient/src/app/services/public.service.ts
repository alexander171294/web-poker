import { Injectable } from '@angular/core';
import { SignupRequest } from './SignupRequest';
import { SessionInformation } from './SessionInformation';
import { environment } from 'src/environments/environment';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { LoginRequest } from './LoginRequest';
import { Session } from 'protractor';
import { ValidateRequest } from './ValidateRequest';

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

  public validate(data: ValidateRequest): Observable<SessionInformation> {
    const url = environment.apiServer + 'public/validate';
    return this.httpClient.post<SessionInformation>(url, data);
  }

}
