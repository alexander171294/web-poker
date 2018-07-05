import { LoginOut } from './dto/LoginOut';
import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Settings } from '../../settings';
import { RegistroIn } from './dto/RegistroIn';
import { Response } from './dto/Response';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(private http: HttpClient) { }

  public registrarme(data: RegistroIn): Observable<Response> {
    const url = Settings.getUrl() + 'userRest/registro';
    return this.http.post<Response>(url, data);
  }

  public login(email: string, password: string): Observable<LoginOut> {
    const url = Settings.getUrl() + 'userRest/login';
    let params = new HttpParams();
    params = params.append('email', email);
    params = params.append('password', password);
    return this.http.get<LoginOut>(url, {params: params});
  }

}
