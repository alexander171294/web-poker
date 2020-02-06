import { Component, OnInit, Input } from '@angular/core';
import { RoomResponse } from 'src/app/services/roomsResponse';
import { Router } from '@angular/router';

@Component({
  selector: 'app-table',
  templateUrl: './table.component.html',
  styleUrls: ['./table.component.scss']
})
export class TableComponent implements OnInit {

  public tab = 1;
  @Input() rooms: RoomResponse[];

  constructor(private router: Router) { }

  ngOnInit() {
    // for(let i = 1; i<=10; i++) {
    //   this.rooms.push({
    //     name: 'Room name #'+i,
    //     description: 'Room description #'+i,
    //     actualPlayers: 5+i,
    //     maxPlayers: 11+i,
    //     minChips: 500*i
    //   });
    // }
  }

  connect(room: RoomResponse) {
    sessionStorage.setItem('room-' + room.id_room, JSON.stringify(room));
    this.router.navigate(['/room', room.id_room]);
  }

  reload() {

  }

}
