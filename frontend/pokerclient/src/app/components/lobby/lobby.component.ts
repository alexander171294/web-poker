import { Component, OnInit } from '@angular/core';
import { Settings } from '../../../settings';

@Component({
  selector: 'app-lobby',
  templateUrl: './lobby.component.html',
  styleUrls: ['./lobby.component.css'],
  moduleId: module.id,
})
export class LobbyComponent implements OnInit {

  public version = Settings.VERSION;

  constructor() { }

  ngOnInit() {
  }

}
