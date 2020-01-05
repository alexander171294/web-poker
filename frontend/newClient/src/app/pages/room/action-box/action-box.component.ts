import { Component, OnInit } from '@angular/core';
import { RoomService } from 'src/app/services/network/room.service';
import { RxEType, ReactionEvents } from 'src/app/services/network/ReactionEvents';

@Component({
  selector: 'app-action-box',
  templateUrl: './action-box.component.html',
  styleUrls: ['./action-box.component.scss']
})
export class ActionBoxComponent implements OnInit {

  isMyTurn: boolean;
  private myPosition = 0;

  public actualRaise: number;
  public minRaise: number;
  public maxRaise: number;
  public toCall: number;
  public canCheck: boolean;

  constructor(private room: RoomService) { }

  ngOnInit() {
    this.room.reactionEvent.subscribe(evt => {
      if (evt.type === RxEType.INGRESS) {
        this.myPosition = evt.data.position;
      }
      if (evt.type === RxEType.WAITING_FOR) {
        // data.position+' for: '+data.remainingTime
        if (this.myPosition === evt.data.position) {
          this.isMyTurn = true;
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

}
