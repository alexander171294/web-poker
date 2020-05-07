import { ChatMessageData } from './ChatMessageData';
import { Component, OnInit, OnDestroy } from '@angular/core';
import { RoomService } from 'src/app/services/network/room.service';
import { RxEType } from 'src/app/services/network/ReactionEvents';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-chat-box',
  templateUrl: './chat-box.component.html',
  styleUrls: ['./chat-box.component.scss']
})
export class ChatBoxComponent implements OnInit, OnDestroy {

  opened: boolean;
  public chatMessages: ChatMessageData[] = [];
  public message: string;

  public playersInPositions: string[] = [];
  public rxESubscription: Subscription;

  constructor(private room: RoomService) { }

  ngOnInit() {
    this.opened = true;
    this.rxESubscription = this.room.reactionEvent.subscribe(evt => {
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
      if (evt.type === RxEType.FOLD) {
        this.chatMessages.push(
          new ChatMessageData(
            false,
            'Fold.',
            this.playersInPositions[evt.data.position]
          )
        );
      }
      if (evt.type === RxEType.DECISION_INFORM) {
        if (evt.data.ammount === 0) {
          return;
        }
        this.chatMessages.push(
          new ChatMessageData(
            false,
            'I go with $' + evt.data.ammount,
            this.playersInPositions[evt.data.position]
          )
        );
      }
      if (evt.type === RxEType.DEPOSIT_ANNOUNCEMENT) {
        this.chatMessages.push(
          new ChatMessageData(
            false,
            'I do a deposit of $' + evt.data.quantity,
            this.playersInPositions[evt.data.position]
          )
        );
      }
      if (evt.type === RxEType.RESULT_SET) {
        evt.data.winners.forEach(winner => {
          this.chatMessages.push(
            new ChatMessageData(
              false,
              'I won $' + winner.pot + ', because I have ' + winner.reason,
              this.playersInPositions[winner.position]
            )
          );
        });
      }
      if (evt.type === RxEType.CHAT) {
        this.chatMessages.push(
          new ChatMessageData(
            false,
            evt.data.message,
            evt.data.author
          )
        );
      }
      // TODO: improve this ugly code:
      setTimeout(() => {
        const cbl = document.getElementById('chatBoxList');
        cbl.scrollTop = cbl.scrollHeight;
      }, 100);
    });
  }

  sendMessageKP(evt) {
    if (evt.keyCode === 13) {
      this.sendMessage();
    }
  }

  sendMessage() {
    this.room.chatMessage(this.message);
    this.message = '';
  }

  ngOnDestroy(): void {
    this.rxESubscription.unsubscribe();
  }

}
