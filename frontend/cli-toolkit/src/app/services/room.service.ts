import { Injectable } from '@angular/core';
import { WsRoomService } from './room/ws-room.service';
import { TerminalService } from './terminal.service';
import { environment } from 'src/environments/environment';
import { EventWS } from '../utils/EventWS';
import { EventTypeWS } from '../utils/EventTypeWS';
import { MessageDefinition } from '../utils/MessageDefinition';
import { Authorization } from '../epprProtocol/userAuth/Authorization';

@Injectable({
  providedIn: 'root'
})
export class RoomService {

  readonly serviceName = 'RoomServer'; 

  constructor(private ws: WsRoomService, private terminal: TerminalService) {

  }

  public connect(serverIP: string) {

    let res = /([0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}):([0-9]+)/gm.exec(serverIP);
    if(res.length > 2) {
      this.terminal.log('Connecting to ip: ['+res[1]+'] at port {'+res[2]+'}');
      if(environment.debugMode) {
        this.ws.wsEventSubscriptor.subscribe((data: EventWS) => {
          let prefix = '{';
          if(data.eventType == EventTypeWS.CONFIGURING) {
            prefix += 'Configuring';
          }
          if(data.eventType == EventTypeWS.CONNECTED) {
            prefix += 'Connected';
            this.subscriptions();
          }
          if(data.eventType == EventTypeWS.CONNECTING) {
            prefix += 'Connecting';
          }
          if(data.eventType == EventTypeWS.DISCONNECT) {
            prefix += 'Disconnect';
          }
          if(data.eventType == EventTypeWS.DISCONNECTED) {
            prefix += 'Disconnected';
          }
          if(data.eventType == EventTypeWS.ERROR) {
            prefix += 'Error';
          }
          if(data.eventType == EventTypeWS.FIRST_CONNECTION) {
            prefix += 'First Connection';
          }
          if(data.eventType == EventTypeWS.FULL_CONNECTION) {
            prefix += 'Full Connection';
          }
          if(data.eventType == EventTypeWS.MESSAGE) {
            prefix += 'Message';
          }
          if(data.eventType == EventTypeWS.SENDING) {
            prefix += 'Sending';
          }
          if(data.eventType == EventTypeWS.SUSCRIPTION) {
            prefix += 'Suscription';
          }
          prefix += '}';
          this.terminal.dlog(prefix + ' ' + JSON.stringify(data.data));
        });
      }
      this.ws.connect(res[1], res[2]);
    } else {
      this.terminal.err('Room server ip doesn\'t match with the regex: /([0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}):([0-9]+)/');
    } 
  }

  authorization(userID: number) {
    this.terminal.out('Authorization [' + userID + ']', this.serviceName);
    const auth = new Authorization();
    auth.userID = userID;
    const dBlock = new MessageDefinition();
    dBlock.data = auth;
    dBlock.endpoint = '/user/authorization';
    dBlock.prefix = '/stompApi';
    this.ws.sendMessage(dBlock);

  }

  onAuthorizationResponse(data: any) {
    console.log(data);
    this.terminal.in('Challenge Claim [' + data.claimToken + '] roomID: [' + data.roomID + ']', this.serviceName);
  }

  ingress(user: string, photo: string) {
    this.terminal.out('Ingress ['+user+']', this.serviceName);
    const dBlock = new MessageDefinition();
    dBlock.data = {user, photo};
    dBlock.endpoint = '/clientInterceptor';
    dBlock.prefix = '/clientInterceptor';
    
  }

  deposit(userID: number, coins: number, challengeID: number, claimToken: string) {

  }

  backwardValidation(challengeID: number) {

  }

  subscriptions() {
    this.ws.suscribe('/userInterceptor/AuthController/challenge', (data) => {
      this.onAuthorizationResponse(data);
    });

  }
}
