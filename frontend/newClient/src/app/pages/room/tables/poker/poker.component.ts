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

  public tableCards: Card[];
  public availablePositions: boolean[] = [];
  public pot: number;
  public dealed: boolean;
  public myPosition: number;

  public info: string;

  constructor(private room: RoomService) { }

  ngOnInit() {
    this.players = [];
    this.tableCards = [];
    for (let i = 0; i < PokerComponent.MAX_PLAYERS; i++) {
      this.players.push(new PlayerSnapshot());
      this.availablePositions.push(false);
    }
    this.room.reactionEvent.subscribe(evt => {
      if (evt.type === RxEType.ANNOUNCEMENT) {
        // this.announcement =
        const nPlayer = new PlayerSnapshot();
        nPlayer.playerDetails.chips = evt.data.chips;
        nPlayer.playerDetails.image = evt.data.avatar;
        nPlayer.playerDetails.name = evt.data.user;
        this.players[evt.data.position] = nPlayer;
        this.availablePositions[evt.data.position] = false;
      }
      if (evt.type === RxEType.START_IN) {
        this.info = 'Game start in ' + evt.data + (evt.data !== 1 ? ' seconds' : ' second');
      }
      if (evt.type === RxEType.ROUND_START) {
        this.info = undefined; // removing info box
      }
      if (evt.type === RxEType.BLINDS) {
        this.pot = evt.data.sbChips + evt.data.bbChips;
        this.players[evt.data.sbPosition].playerDetails.chips -= evt.data.sbChips;
        this.players[evt.data.bbPosition].playerDetails.chips -= evt.data.bbChips;
        this.players[evt.data.sbPosition].actualBet = evt.data.sbChips;
        this.players[evt.data.bbPosition].actualBet = evt.data.bbChips;
        this.dealed = true;
      }
      if (evt.type === RxEType.CARD_DIST) {
        if (evt.data.position !== 1) {
          this.players[evt.data.position].upsidedown = true;
          this.players[evt.data.position].cards = evt.data.cards;
        }
      }
      if (evt.type === RxEType.ME_CARD_DIST) {
        this.players[evt.data.position].upsidedown = false;
        this.players[evt.data.position].cards = evt.data.cards;
      }
      if (evt.type === RxEType.WAITING_FOR) {
        // data.position+' for: '+data.remainingTime
        if (this.tableCards.length === 0) {
          this.tableCards = [null, null, null, null, null];
        }
        this.players.forEach((player, idx) => {
          if (player) {
            this.players[idx].timeRest = idx === evt.data.position ? evt.data.remainingTime : undefined;
          }
        });
      }
      if (evt.type === RxEType.DONE_ACTION) {
        this.players[this.myPosition].timeRest = undefined;
      }
      if (evt.type === RxEType.INGRESS) {
        this.myPosition = evt.data.position;
        for (let i = 0; i < PokerComponent.MAX_PLAYERS; i++) {
          this.availablePositions[i] = false;
        }
      }
      if (evt.type === RxEType.FLOP) {
        this.tableCards[0] = evt.data[0];
        this.tableCards[1] = evt.data[1];
        this.tableCards[2] = evt.data[2];
      }
      if (evt.type === RxEType.TURN) {
        this.tableCards[3] = evt.data;
      }
      if (evt.type === RxEType.RIVER) {
        this.tableCards[4] = evt.data;
      }
      if (evt.type === RxEType.SNAPSHOT) {
        console.log('SNAPSHOT', evt.data);
        evt.data.players.forEach((player, idx) => {
          console.log(idx, player);
          if (player != null) {
            const nPlayer = new PlayerSnapshot();
            nPlayer.playerDetails.chips = player.chips;
            nPlayer.playerDetails.image = player.photo;
            nPlayer.playerDetails.name = player.nick;
            this.players[idx] = nPlayer;
          }
        });
      }
      if (evt.type === RxEType.DEFINE_POSITION) {
        evt.data.forEach(freePositions => {
          this.availablePositions[freePositions] = true;
        });
      }
      if (evt.type === RxEType.DECISION_INFORM) {
        console.info('Decision Inform', evt.data);
      }
    });
  }

  trySeat(position: number) {
    if (this.availablePositions[position]) {
      if (this.players[position].playerDetails.name) {
        // TODO: improve this alert:
        alert('This position is in use.');
      } else {
        console.warn('Sitting...');
        this.room.selectPosition(position);
      }
    }
  }

}
