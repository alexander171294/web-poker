import { Component, OnInit } from '@angular/core';
import { PublicService } from 'src/app/services/public.service';
import { LoginRequest } from 'src/app/services/LoginRequest';
import { SignupRequest } from 'src/app/services/SignupRequest';
import { Router, ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {

  public signupEffect: boolean = false;
  public signupForm: boolean = false;
  public logining: boolean = false;
  public signing: boolean = false;
  public errorLogin: boolean = false;
  public errorRegister: boolean = false;

  public nick: string;
  public password: string;
  public email: string;

  constructor(private publicSrv: PublicService, private router: Router, private route: ActivatedRoute) { }

  ngOnInit() {
    const ec = this.route.snapshot.paramMap.get('err');
    if (ec === '401' || ec === '403') {
      this.errorLogin = true;
      // sessión expirada por favor reloguee.
    }

  }

  signup() {
    this.signupEffect = true;
    setTimeout(()=>{
      this.signupForm = true;
    }, 300);
  }

  login() {
    this.signupEffect = false;
    setTimeout(()=>{
      this.signupForm = false;
    }, 300);
  }

  doLogin(e) {
    this.errorLogin = false;
    this.logining = true;
    const data = new LoginRequest();
    data.umail = this.nick;
    data.password = this.password;
    this.publicSrv.login(data).subscribe(data => {
      if(data.operationSuccess) {
        localStorage.setItem('jwt', data.jwtToken);
        localStorage.setItem('sessID', data.sessionID);
        localStorage.setItem('userID', data.userID);
        this.router.navigate(['/lobby']);
      } else {
        alert(data.errorDescription);
      }
      this.logining = false;

    }, err => {
      //alert('ocurrió un error');
      // console.log(err);
      this.logining = false;
      this.errorLogin = true;

    });
    e.preventDefault();
  }

  doSignup(e) {
    this.errorRegister = false;
    this.signing = true;
    const data = new SignupRequest();
    data.nick_name = this.nick;
    data.email = this.email;
    data.password = this.password;
    this.publicSrv.signup(data).subscribe(data => {
      if(data.operationSuccess) {
        localStorage.setItem('jwt', data.jwtToken);
        localStorage.setItem('sessID', data.sessionID);
        localStorage.setItem('userID', data.userID);
        this.router.navigate(['/lobby']);
      } else {
        alert(data.errorDescription);
      }
      this.signing = false;
    }, err => {
      //alert('ocurrió un error');
      this.errorRegister = true;
      // console.log(err);
      this.signing = false;
    });
    e.preventDefault();
  }
}
