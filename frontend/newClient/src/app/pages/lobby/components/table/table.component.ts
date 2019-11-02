import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-table',
  templateUrl: './table.component.html',
  styleUrls: ['./table.component.scss']
})
export class TableComponent implements OnInit {

  public rooms: any[] = [];

  constructor() { }

  ngOnInit() {
    for(let i = 1; i<=10; i++) {
      this.rooms.push({
        name: 'Room name #'+i,
        description: 'Room description #'+i,
        actualPlayers: 5+i,
        maxPlayers: 11+i,
        minChips: 500*i
      });
    }
  }

}
