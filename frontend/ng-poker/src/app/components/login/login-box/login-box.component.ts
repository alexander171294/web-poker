import { LoginOut } from './../../../services/dto/LoginOut';
import { UserService } from './../../../services/user.service';
import { Component, OnInit } from '@angular/core';
import { StatusCodes } from '../../../services/dto/Response';

@Component({
  selector: 'app-login-box',
  templateUrl: './login-box.component.html',
  styleUrls: ['./login-box.component.css']
})
export class LoginBoxComponent implements OnInit {

  public email: string;
  public password: string;

  constructor(private user: UserService) { }

  ngOnInit() {

  }

  signin() {
    this.user.login(this.email, this.password).subscribe((data) => this.onLogin(data), (error) => this.onError(error));
  }

  public onLogin(data: LoginOut) {
    if (data.statusCode === StatusCodes.OK) {
      // REDIRIGIR
      console.log('LOGIN OK');
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
