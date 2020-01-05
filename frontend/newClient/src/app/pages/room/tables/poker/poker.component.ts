import { RoomService } from 'src/app/services/network/room.service';
import { Component, OnInit } from '@angular/core';
import { PlayerSnapshot } from './PlayerSnapshot';
import { Card } from '../../cards/dual-stack/Card';
import { WsRoomService } from 'src/app/services/network/room/ws-room.service';
import { RxEType } from 'src/app/services/network/ReactionEvents';

@Component({
  selector: 'app-table-poker',
  templateUrl: './poker.component.html',
  styleUrls: ['./poker.component.scss']
})
export class PokerComponent implements OnInit {

  private static MAX_PLAYERS = 10;

  public players: PlayerSnapshot[];

  private tableCards: Card[];
  private pot: number;
  private dealed: boolean;

  public info: string;

  constructor(private room: RoomService) { }

  ngOnInit() {
    this.players = [];
    this.tableCards = [];
    for (let i = 0; i < PokerComponent.MAX_PLAYERS; i++) {
      this.players.push(new PlayerSnapshot());
    }
    this.room.reactionEvent.subscribe(evt => {
      if (evt.type === RxEType.ANNOUNCEMENT) {
        // this.announcement =
        const nPlayer = new PlayerSnapshot();
        nPlayer.playerDetails.chips = evt.data.chips;
        nPlayer.playerDetails.image = evt.data.avatar;
        nPlayer.playerDetails.name = evt.data.user;
        this.players[evt.data.position] = nPlayer;
      }
      if (evt.type === RxEType.START_IN) {
        this.info = 'Game start in ' + evt.data + (evt.data !== 1 ? ' seconds' : ' second');
      }
      if (evt.type === RxEType.ROUND_START) {

      }
      if (evt.type === RxEType.BLINDS) {

      }
      if (evt.type === RxEType.CARD_DIST) {

      }
      if (evt.type === RxEType.ME_CARD_DIST) {

      }
    });
  }

  trySeat(position: number) {
    if (this.players[position].playerDetails.name) {
      // TODO: improve this alert:
      alert('This position is in use.');
    } else {
      console.warn('Sitting...');
      this.room.selectPosition(position);
    }
  }

}
