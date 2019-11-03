import { Component, OnInit, Input } from '@angular/core';
import { RoomResponse } from 'src/app/services/roomsResponse';
import { Router } from '@angular/router';

@Component({
  selector: 'app-table',
  templateUrl: './table.component.html',
  styleUrls: ['./table.component.scss']
})
export class TableComponent implements OnInit {

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

  connect(serverIp: string) {
    sessionStorage.setItem('server', serverIp);
    this.router.navigate(['/room']);
  }

}
