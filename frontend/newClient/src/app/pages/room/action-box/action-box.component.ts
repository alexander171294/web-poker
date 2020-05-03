import { BetSelectorComponent } from './bet-selector/bet-selector.component';
import { ChipsService } from './../../../services/memory/chips.service';
import { PlayerSnapshot } from './../tables/poker/PlayerSnapshot';
import { Component, OnInit, ViewChild } from '@angular/core';
import { RoomService } from 'src/app/services/network/room.service';
import { RxEType } from 'src/app/services/network/ReactionEvents';

@Component({
  selector: 'app-action-box',
  templateUrl: './action-box.component.html',
  styleUrls: ['./action-box.component.scss']
})
export class ActionBoxComponent implements OnInit {

  isMyTurn: boolean;
  private myPosition = 0;

  public actualRaise = -1;
  public minRaise: number;
  public maxRaise: number;
  public actualBet: number;
  public toCall: number;
  public canCheck: boolean;
  public mySnapshot: PlayerSnapshot = new PlayerSnapshot();
  public myChips = 0;
  @ViewChild('betSelector', {static: false}) betSelector: BetSelectorComponent;

  constructor(private room: RoomService, private chips: ChipsService) { }

  ngOnInit() {
    this.room.reactionEvent.subscribe(evt => {
      if (evt.type === RxEType.INGRESS) {
        this.myPosition = evt.data.position;
      }
      if (evt.type === RxEType.WAITING_FOR) {
        // data.position+' for: '+data.remainingTime
        if (this.myPosition === evt.data.position) {
          this.isMyTurn = true;
          // console.log('Mis fichas: ', this.chips.get());
          this.actualBet = this.mySnapshot.actualBet ?  this.mySnapshot.actualBet : 0;
          // console.log('Actual Bet:', this.actualBet);
          this.myChips = this.chips.get();
        } else {
          this.isMyTurn = false;
        }
      }
      if (evt.type === RxEType.BET_DECISION) {
        // data.toCall+' can check? ' + (data.canCheck ? '[Yes]' : '{No}') + ' Raise >' + data.minRaise + ' and <' + data.maxRaise
        this.minRaise = evt.data.minRaise;
        this.maxRaise = evt.data.maxRaise;
        this.actualRaise = evt.data.minRaise;
        this.canCheck = evt.data.canCheck;
        this.toCall = evt.data.toCall;
      }
      if (evt.type === RxEType.DONE_ACTION) {
        this.isMyTurn = false;
      }
      if (evt.type === RxEType.SNAPSHOT) {
        if (evt.data.waitingFor && evt.data.waitingFor === evt.data.myPosition) {
          this.isMyTurn = true;
          // console.log('Mis fichas: ', this.chips.get());
          this.myChips = this.chips.get();
          this.actualBet = this.mySnapshot.actualBet ?  this.mySnapshot.actualBet : 0;
          // console.log('Actual Bet:', this.actualBet);
          this.minRaise = evt.data.betDecision.minRaise;
          this.maxRaise = evt.data.betDecision.maxRaise;
          this.actualRaise = evt.data.betDecision.minRaise;
          this.canCheck = evt.data.betDecision.canCheck;
          this.toCall = evt.data.betDecision.toCall;
        }
      }
    });
    this.isMyTurn = false;
  }

  call(quantity: number) {
    this.room.bridge('call');
  }

  check() {
    this.room.bridge('check');
  }

  fold() {
    this.room.bridge('fold');
  }

  raise(quantity: number) {
    this.room.bridge('raise', quantity);
  }

  multiply(multiplier: number) {
    const bet = this.chips.getBigBlind() * multiplier;
    const myChips = this.chips.get();
    // console.log(myChips);
    if (bet > myChips) {
      this.actualRaise = myChips;
    } else {
      this.actualRaise = bet;
    }
    if (this.betSelector) {
      this.betSelector.refreshBet(this.actualRaise);
    }
  }

  allIn() {
    // console.log(this.chips.get(), this.toCall);
    this.actualRaise = this.toCall > this.chips.get() ? 0 : this.chips.get() - this.toCall;
    if (this.betSelector) {
      this.betSelector.refreshBet(this.actualRaise);
    }
  }

}
