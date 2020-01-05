import { Component, OnInit } from '@angular/core';
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
    });
    this.isMyTurn = false;
  }

}
