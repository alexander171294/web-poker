import { Injectable } from '@angular/core';
import { WsRoomService } from './room/ws-room.service';
import { TerminalService } from './terminal.service';
import { environment } from 'src/environments/environment';
import { EventWS } from '../utils/EventWS';
import { EventTypeWS } from '../utils/EventTypeWS';
import { MessageDefinition } from '../utils/MessageDefinition';
import { Authorization } from '../epprProtocol/userAuth/Authorization';
import { BackwardValidation } from '../epprProtocol/userAuth/BackwardValidation';
import { ChallengeActions } from '../epprProtocol/userAuth/types/ChallengeActions';

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
      // if(environment.debugMode) {
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
            prefix += 'Receiving';
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
      // }
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
    
  }

  deposit(userID: number, coins: number, challengeID: number, claimToken: string) {

  }

  backwardValidation(challengeID: number) {
    this.terminal.out('Backward Validation CID ['+challengeID+']', this.serviceName);
    const bV = new BackwardValidation();
    bV.action = ChallengeActions.LOGIN;
    bV.idChallenge = challengeID;
    const dBlock = new MessageDefinition();
    dBlock.data = bV;
    dBlock.endpoint = '/user/backwardValidation';
    dBlock.prefix = '/stompApi';
    this.ws.sendMessage(dBlock);
  }

  subscriptions() {
    this.ws.suscribe('/userInterceptor/AuthController/challenge', (data) => {
      this.onAuthorizationResponse(data);
    });
    this.ws.suscribe('/userInterceptor/AuthController/rejected', (data) => {
      this.terminal.in('UserAuth Rejected', this.serviceName);
    });
    this.ws.suscribe('/userInterceptor/AuthController/response', (data) => this.onAuthResponse(data));
    this.ws.suscribe('/userInterceptor/AuthController/kick', (data) => this.onKick(data));
    this.ws.suscribe('/userInterceptor/GameController/definePosition', (data) => this.onDefinePosition(data));
    this.ws.suscribe('/userInterceptor/GameController/deposit', (data) => this.onDeposit(data));
    this.ws.suscribe('/userInterceptor/GameController/rejectFullyfied', (data) => this.onRejectFullyfied(data));
    this.ws.suscribe('/userInterceptor/GameController/announcement', (data) => this.onAnnouncement(data));
    this.ws.suscribe('/userInterceptor/GameController/ingress', (data) => this.onIngress(data));
  }

  onIngress(data) {
    this.terminal.in('Ingress Chips: '+data.chips+' - position: '+data.position, this.serviceName);
  }

  onAnnouncement(data) {
    this.terminal.in('Announcement: Pos['+data.position+'] user['+data.user+'] chips['+data.chips+'] avatar['+data.avatar+']', this.serviceName);
  }

  onRejectFullyfied(data) {
    this.terminal.in('REJECTED Fullyfied', this.serviceName);
  }

  onDefinePosition(data) {
    this.terminal.in('Define Position, free positions: '+JSON.stringify(data.positions), this.serviceName);
  }

  onDeposit(data) {
    this.terminal.in('Requesting deposit...', this.serviceName);
  }

  onAuthResponse(dataResponse) {
    console.log('DR AutValidated', dataResponse);
    if (dataResponse.schema == 'validated') {
      this.terminal.in('Validated :) Schema', this.serviceName);
    }
    if (dataResponse.schem == 'badRequest') {
      this.terminal.in('Bad request schema', this.serviceName);
    }
    if (dataResponse.schem == 'fullRejected') {
      this.terminal.in('Full rejected (Banned) Schema', this.serviceName);
    }
  }

  onKick(dataResponse) {
    console.log('DR Kick', dataResponse);
    this.terminal.info('You are kicked from this server');
  }
}
