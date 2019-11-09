import { Component, OnInit } from '@angular/core';
import { PlayerSnapshot } from './PlayerSnapshot';
import { Card } from '../../cards/dual-stack/Card';

@Component({
  selector: 'app-table-poker',
  templateUrl: './poker.component.html',
  styleUrls: ['./poker.component.scss']
})
export class PokerComponent implements OnInit {

  private players: PlayerSnapshot[];
  private static MAX_PLAYERS = 10;

  private tableCards: Card[];
  private pot: number;
  private dealed: boolean;

  constructor() { }

  ngOnInit() {
    this.players = [];
    this.tableCards = [];
    for(let i = 0; i<PokerComponent.MAX_PLAYERS; i++) {
      this.players.push(new PlayerSnapshot());
    }
  }

}
