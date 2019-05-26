import { Component, OnInit } from '@angular/core';
import { LobbyService } from 'src/app/providers/lobby/lobby.service';
import { RoomsResponse, RoomResponse } from 'src/app/providers/lobby/roomsResponse';

@Component({
  selector: 'app-body',
  templateUrl: './body.component.html',
  styleUrls: ['./body.component.scss']
})
export class BodyComponent implements OnInit {

  rooms: RoomResponse[];

  constructor(private lobbySrv: LobbyService) { }

  ngOnInit() {
    this.rooms = [];
    this.updateRooms();
  }

  updateRooms() {
    this.rooms = [];
    this.lobbySrv.getRooms().subscribe(
      (data) => this.onRoomsUpdateds(data),
      (err) => {
        alert('Connection error.');
      }
    );
  }

  onRoomsUpdateds(data: RoomsResponse) {
    this.rooms = data.rooms;
  }

}
