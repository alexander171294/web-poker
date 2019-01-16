import { LoginOut } from './../../../services/dto/LoginOut';
import { UserService } from './../../../services/user.service';
import { Component, OnInit } from '@angular/core';
import { StatusCodes } from '../../../services/dto/Response';
import { Router } from '@angular/router';
import { SessionData } from '~/app/utils/sessionData';

@Component({
  selector: 'app-login-box',
  templateUrl: './login-box.component.html',
  styleUrls: ['./login-box.component.css']
})
export class LoginBoxComponent implements OnInit {

  public email: string;
  public password: string;

  constructor(private user: UserService, private router: Router) { }

  ngOnInit() {

  }

  signin() {
    this.user.login(this.email, this.password).subscribe((data) => this.onLogin(data), (error) => this.onError(error));
  }

  public onLogin(data: LoginOut) {
    if (data.statusCode === StatusCodes.OK) {

      // REDIRIGIR
      console.log('LOGIN OK');
      sessionStorage.setItem('user_id', data.id.toString());
      sessionStorage.setItem('user_hash', data.upgrade);
      const sessionData: SessionData = new SessionData();
      const fechaActual = new Date();
      sessionData.IssuedAt = fechaActual.getTime() / 1000;
      sessionData.Issuer = 'NS-LOGIN'; // change this with specific platform.
      sessionData.notBefore = fechaActual.getTime() / 1000;
      sessionData.Secret = data.upgrade; // Token que manda el backend del campo correspondiente en tabla usuario
      sessionData.Subject = data.id.toString(); // ID DEL USUARIO EN CUESTIÓN QUE SE LOGUEÓ
      sessionData.TrackingId = Math.floor(Math.random() * 90000) + 10000;
      const fechaExpiracion = new Date();
      sessionData.Expiration = Math.floor(
        (fechaExpiracion.getTime() + 1 * 60 * 60 * 1000) / 1000
      );
      // guardamos la sesión:
      sessionData.saveData('loginJWTData');

      this.router.navigate(['lobby']);
      // guardar id
    } else {
      alert(data.message);
      console.log(data, StatusCodes.OK);
    }
  }

  public onError(error) {
    alert('Error de conexion');
  }

}
