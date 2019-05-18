import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { PublicService } from '../providers/public/public.service';
import { SignupRequest } from '../providers/public/SignupRequest';
import { LoginRequest } from '../providers/public/LoginRequest';
import { SessionService } from '../providers/session.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {

  loginEmail: string;
  loginPassword: string;

  signupEmail: string;
  signupNick: string;
  signupPassword: string;
  signupPassword2: string;

  isSignup: boolean; // loading...
  isSignin: boolean; // loading...

  constructor(private router: Router, private publicSrv: PublicService, private session: SessionService) { }

  ngOnInit() {
  }

  signUp() {
    if(this.signupNick.length<3 || this.signupNick.length>12) {
      // TODO: show local error
      return;
    }
    // TODO: validate email.
    if(this.signupPassword != this.signupPassword2) {
      // TODO: show local error
      return;
    }
    if(!this.isSignup && !this.isSignin) {
      this.isSignup = true;
      const data = new SignupRequest();
      data.email = this.signupEmail;
      data.nick_name = this.signupNick;
      data.password = this.signupPassword;
      // data.photo = 
      this.publicSrv.signup(data).subscribe((response) => {
        this.isSignup = false;
        if (response.operationSuccess) {
          this.session.setSessionInfo(response);
          this.router.navigate(['/lobby']);
        } else {
          // TODO: improve alert.
          alert(response.errorDescription);
        }
      }, (err) => {
        console.log();
        // TODO: improve error and catch error codes.
        if(err.status == 400 && err.error) {
          alert(err.error.errorDescription);
        } else {
          alert('Connection error.');
        }
        
        this.isSignup = false;
      });
      // this.router.navigate(['/lobby']);
    }
  }

  signIn() {
    if(!this.isSignup && !this.isSignin) {
      this.isSignin = true;
      // TODO: validations
      const data = new LoginRequest();
      data.umail = this.loginEmail;
      data.password = this.loginPassword;
      this.publicSrv.login(data).subscribe((data) => {
        console.log(data);
        if(data.operationSuccess) {
          this.session.setSessionInfo(data);
          this.router.navigate(['/lobby']);
        } else {
          // TODO: improve alert.
          alert(data.errorDescription);
        }
        this.isSignin = false;
      }, (err) => {
        console.log();
        // TODO: improve error and catch error codes.
        if(err.status == 400 && err.error) {
          alert(err.error.errorDescription);
        } else {
          alert('Connection error.');
        }
        this.isSignin = false;
      });
    }
  }

}
