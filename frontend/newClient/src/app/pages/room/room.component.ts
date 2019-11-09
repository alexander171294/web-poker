import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Params } from '@angular/router';
import { RoomResponse } from 'src/app/services/roomsResponse';
import { RoomService } from 'src/app/services/network/room.service';
import { TerminalService } from 'src/app/services/network/terminal.service';
import { WsRoomService } from 'src/app/services/network/room/ws-room.service';
import { Authorization } from 'src/app/services/network/epprProtocol/userAuth/Authorization';
import { MessageDefinition } from 'src/app/services/network/utils/MessageDefinition';

@Component({
  selector: 'app-room',
  templateUrl: './room.component.html',
  styleUrls: ['./room.component.scss']
})
export class RoomComponent implements OnInit {

  public roomID: number;
  public roomData: RoomResponse;

  constructor(private route: ActivatedRoute,
              private ws: WsRoomService,
              private room: RoomService,
              private terminal: TerminalService) {
    terminal.event.subscribe(data => {
      console.log('[]> '+data);
    });
    terminal.errorEvent.subscribe(data => {
      console.error('[]> '+data);
    });
    terminal.infoEvent.subscribe(data => {
      console.warn('[]> '+data);
    });
    terminal.noteEvent.subscribe(data => {
      console.log('!!! '+data);
    });
    terminal.debugEvents.subscribe(data => {
      console.log('------------------> '+data);
    });
  }

  ngOnInit() {
    this.roomID = this.route.params['value'].id; // this.route.snapshot.queryParamMap.get('id');
    this.roomData = JSON.parse(sessionStorage.getItem('room-' + this.roomID));
    this.room.connect(this.roomData.server_ip);
    //console.log(this.roomData);
  }

  private authorization(userID: number) {
    console.log('AUTHORIZING USER ', userID);
    const auth = new Authorization();
    auth.userID = userID;
    const dBlock = new MessageDefinition();
    dBlock.data = auth;
    dBlock.endpoint = '/user/authorization';
    dBlock.prefix = '/stompApi';
    this.ws.sendMessage(dBlock);
  }

}
