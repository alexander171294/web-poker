import { Settings } from './../../../settings';
import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
  moduleId: module.id,
})
export class LoginComponent implements OnInit {

  public version = Settings.VERSION;

  constructor() { }

  ngOnInit() {
  }

}
