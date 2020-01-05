import { ChatMessageData } from './ChatMessageData';
import { Component, OnInit } from '@angular/core';
import { RoomService } from 'src/app/services/network/room.service';
import { RxEType } from 'src/app/services/network/ReactionEvents';

@Component({
  selector: 'app-chat-box',
  templateUrl: './chat-box.component.html',
  styleUrls: ['./chat-box.component.scss']
})
export class ChatBoxComponent implements OnInit {

  opened: boolean;
  public chatMessages: ChatMessageData[] = [];

  public playersInPositions: string[] = [];

  constructor(private room: RoomService) { }

  ngOnInit() {
    this.opened = false;
    this.room.reactionEvent.subscribe(evt => {
      if (evt.type === RxEType.ANNOUNCEMENT) {
        this.playersInPositions[evt.data.position] = evt.data.user;
        this.chatMessages.push(new ChatMessageData(true, '@' + evt.data.user + ' is now in the room.'));
      }
      if (evt.type === RxEType.START_IN) {
        this.chatMessages.push(new ChatMessageData(true, 'Game start in ' + evt.data + (evt.data !== 1 ? ' seconds' : ' second')));
      }
      if (evt.type === RxEType.ROUND_START) {
        this.chatMessages.push(
          new ChatMessageData(
            true,
            'Starting round, Dealer: ' + this.playersInPositions[evt.data.dealerPosition] + '. Round number: ' + evt.data.roundNumber
          )
        );
      }
      if (evt.type === RxEType.BLINDS) {
        this.chatMessages.push(
          new ChatMessageData(
            false,
            'I\'m putting the small blind, $' + evt.data.sbChips,
            this.playersInPositions[evt.data.sbPosition]
          )
        );
        this.chatMessages.push(
          new ChatMessageData(
            false,
            'I\'m putting the big blind, $' + evt.data.bbChips,
            this.playersInPositions[evt.data.bbPosition]
          )
        );
      }
      // if (evt.type === RxEType.CARD_DIST) {

      // }
      // if (evt.type === RxEType.ME_CARD_DIST) {

      // }
    });
  }

}
