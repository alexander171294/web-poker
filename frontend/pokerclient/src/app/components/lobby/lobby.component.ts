import { Component, OnInit, AfterViewInit } from '@angular/core';
import { Settings } from '../../../settings';
import { Rooms } from '~/app/services/dto/Rooms';
import { LobbyServer } from '~/app/services/lobby.server';

@Component({
  selector: 'app-lobby',
  templateUrl: './lobby.component.html',
  styleUrls: ['./lobby.component.css'],
  moduleId: module.id,
})
export class LobbyComponent implements OnInit, AfterViewInit {

  public version = Settings.VERSION;

  public servers: Rooms[];

  constructor(private bcknd: LobbyServer) { }

  ngOnInit() {
  }

  ngAfterViewInit() {
    this.bcknd.getRoomsSitNGo().subscribe(data => {
      this.servers = data;
    }, error => {
      alert('Error getting rooms');
    });
  }


}
