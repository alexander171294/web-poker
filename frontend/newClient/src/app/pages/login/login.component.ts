import { Component, OnInit } from '@angular/core';
import { PublicService } from 'src/app/services/public.service';
import { LoginRequest } from 'src/app/services/LoginRequest';
import { SignupRequest } from 'src/app/services/SignupRequest';
import { Router, ActivatedRoute } from '@angular/router';
import { ValidateRequest } from 'src/app/services/ValidateRequest';

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
  public signupValidation: boolean = false;
  public myID: string;
  public loadingValidationSignup: boolean = false;
  public popupErrorMessage: string;

  public codeValidationSignup: string;

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
      if (data.operationSuccess) {
        localStorage.setItem('jwt', data.jwtToken);
        localStorage.setItem('sessID', data.sessionID);
        localStorage.setItem('userID', data.userID);
        this.router.navigate(['/lobby']);
      } else {
        alert(data.errorDescription);
      }
      this.logining = false;

    }, err => {
      if (err.error.errorCode === 6) {
        this.signupValidation = true;
        this.myID = err.error.userID;
      } else {
        this.errorLogin = true;
      }
      this.logining = false;
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
      if (data.operationSuccess) {
        if (data.requestValidation) {
          this.signupValidation = true;
          this.myID = data.userID;
        } else {
          localStorage.setItem('jwt', data.jwtToken);
          localStorage.setItem('sessID', data.sessionID);
          localStorage.setItem('userID', data.userID);
          this.router.navigate(['/lobby']);
        }
      } else {
        alert(data.errorDescription);
      }
      this.signing = false;
    }, err => {
      this.errorRegister = true;
      this.signing = false;
    });
    e.preventDefault();
  }

  validateSignup() {
    const vr = new ValidateRequest();
    vr.userID = parseInt(this.myID, 10);
    vr.validationCode  = this.codeValidationSignup;
    this.loadingValidationSignup = true;
    this.popupErrorMessage = undefined;
    this.publicSrv.validate(vr).subscribe(d => {
      this.loadingValidationSignup = false;
      if (d.operationSuccess) {
        this.signupValidation = false;
        localStorage.setItem('jwt', d.jwtToken);
        localStorage.setItem('sessID', d.sessionID);
        localStorage.setItem('userID', d.userID);
        this.router.navigate(['/lobby']);
      } else {
        this.popupErrorMessage = d.errorDescription;
      }
    }, e => {
      if (e.error && e.error.errorDescription) {
        this.popupErrorMessage = e.error.errorDescription;
      } else {
        this.popupErrorMessage = 'An error was occurred, please try again.';
      }
      this.loadingValidationSignup = false;
      this.codeValidationSignup = '';
    });
  }
}
