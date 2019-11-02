import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {

  public signupEffect: boolean = false;
  public signupForm: boolean = false;

  constructor() { }

  ngOnInit() {
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

}
