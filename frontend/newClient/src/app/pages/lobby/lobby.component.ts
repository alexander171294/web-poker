import { Component, OnInit } from '@angular/core';
import { environment } from 'src/environments/environment';
import { LobbyService } from 'src/app/services/lobby.service';
import { RoomResponse } from 'src/app/services/roomsResponse';

@Component({
  selector: 'app-lobby',
  templateUrl: './lobby.component.html',
  styleUrls: ['./lobby.component.scss']
})
export class LobbyComponent implements OnInit {

  public version = environment.version;
  private rooms: RoomResponse[];

  constructor(private lobbySrv: LobbyService) { }

  ngOnInit() {
    this.lobbySrv.getRooms().subscribe(rooms => {
      this.rooms = rooms.rooms;
    });
  }

}
