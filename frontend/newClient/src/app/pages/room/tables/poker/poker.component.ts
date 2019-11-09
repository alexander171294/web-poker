import { Component, OnInit } from '@angular/core';
import { PlayerSnapshot } from './PlayerSnapshot';

@Component({
  selector: 'app-table-poker',
  templateUrl: './poker.component.html',
  styleUrls: ['./poker.component.scss']
})
export class PokerComponent implements OnInit {

  private players: PlayerSnapshot[];
  private static MAX_PLAYERS = 10;

  constructor() { }

  ngOnInit() {
    this.players = [];
    for(let i = 0; i<PokerComponent.MAX_PLAYERS; i++) {
      this.players.push(new PlayerSnapshot());
    }
  }

}
