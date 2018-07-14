import { Response, StatusCodes } from './../../../services/dto/Response';
import { Component, OnInit } from '@angular/core';
import { UserService } from '../../../services/user.service';
import { SignupIn } from '../../../services/dto/SignupIn';
import { SignupOut } from '../../../services/dto/SignupOut';

@Component({
  selector: 'app-signup-box',
  templateUrl: './signup-box.component.html',
  styleUrls: ['./signup-box.component.css']
})
export class SignupBoxComponent implements OnInit {

  nick = '';
  email = '';
  passwd = '';
  passwd2 = '';

  constructor(private user: UserService) { }

  ngOnInit() {
  }

  signup() {
    try {
      if (this.nick.length < 3) { throw new Error('El nick debe tener al menos 3 caracteres'); }
      if (this.email.length < 3) { throw new Error('El mail es invalido'); }
      if (this.passwd.length < 8) { throw new Error('La password debe tener al menos 8 caracteres'); }
      if (this.passwd !== this.passwd2) { throw new Error('Las passwords no coinciden'); }
      const data: SignupIn = new SignupIn();
      data.nick = this.nick;
      data.email = this.email;
      data.password = this.passwd;
      this.user.registrarme(data).subscribe((response) => this.onSignupOk(response), (error) => this.onSignupError(error));
    } catch (e) {
      alert(e);
    }
  }

  onSignupError(error) {

  }

  onSignupOk(data: SignupOut) {
    if (data.statusCode === StatusCodes.OK) {
      sessionStorage.setItem('user_id', data.id_usuario.toString());
      sessionStorage.setItem('user_hash', data.upgrade);
    } else {
      alert(data.message);
    }
  }
}
