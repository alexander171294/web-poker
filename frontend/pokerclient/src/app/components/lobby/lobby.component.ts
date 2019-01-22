import { Component, OnInit, AfterViewInit } from '@angular/core';
import { Settings } from '../../../settings';
import { Rooms } from '~/app/services/dto/Rooms';
import { LobbyServer } from '~/app/services/lobby.server';
import { Router } from '@angular/router';

@Component({
  selector: 'app-lobby',
  templateUrl: './lobby.component.html',
  styleUrls: ['./lobby.component.css'],
  moduleId: module.id,
})
export class LobbyComponent implements OnInit, AfterViewInit {

  public version = Settings.VERSION;

  public servers: Rooms[];

  constructor(private bcknd: LobbyServer, private router: Router) { }

  ngOnInit() {
  }

  ngAfterViewInit() {
    this.bcknd.getRoomsSitNGo().subscribe(data => {
      this.servers = data;
    }, error => {
      alert('Error getting rooms');
    });
  }

  connectTo(ip: string, port: number) {
    console.log('ws://' + ip + ':' + port);
    this.router.navigate(['room']);
  }

}
