import { Injectable, EventEmitter } from '@angular/core';
import { WsRoomService } from './room/ws-room.service';
import { TerminalService } from './terminal.service';
import { environment } from 'src/environments/environment';
import { EventWS } from '../utils/EventWS';
import { EventTypeWS } from '../utils/EventTypeWS';
import { MessageDefinition } from '../utils/MessageDefinition';
import { Authorization } from '../epprProtocol/userAuth/Authorization';
import { BackwardValidation } from '../epprProtocol/userAuth/BackwardValidation';
import { ChallengeActions } from '../epprProtocol/userAuth/types/ChallengeActions';
import { SelectPosition } from '../epprProtocol/userAuth/SelectPosition';
import { Deposit } from '../epprProtocol/clientOperations/Deposit';
import { DecisionInform } from '../epprProtocol/game/DecisionInform';

@Injectable({
  providedIn: 'root'
})
export class RoomService {

  readonly serviceName = 'RoomServer'; 
  public actionButtonEvent: EventEmitter<number> = new EventEmitter<number>();

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
    this.terminal.info('Ingress ['+user+']');
    //const dBlock = new MessageDefinition();
    
  }

  deposit(chips: number) {
    this.terminal.out('Request deposit of ' + chips + ' chips', this.serviceName);
    const deposit = new Deposit();
    deposit.chips = chips;
    const dBlock = new MessageDefinition();
    dBlock.data = deposit;
    dBlock.endpoint = '/user/deposit';
    dBlock.prefix = '/stompApi';
    this.ws.sendMessage(dBlock);
  }

  backwardValidation(challengeID: number, deposit: boolean) {
    this.terminal.out('Backward Validation CID ['+challengeID+']', this.serviceName);
    const bV = new BackwardValidation();
    bV.action = deposit ? ChallengeActions.DEPOSIT : ChallengeActions.LOGIN;
    bV.idChallenge = challengeID;
    const dBlock = new MessageDefinition();
    dBlock.data = bV;
    dBlock.endpoint = '/user/backwardValidation';
    dBlock.prefix = '/stompApi';
    this.ws.sendMessage(dBlock);
  }

  selectPosition(position: number) {
    this.terminal.out('Select Position ['+position+']', this.serviceName);
    const sP = new SelectPosition();
    sP.position = position;
    const dBlock = new MessageDefinition();
    dBlock.data = sP;
    dBlock.endpoint = '/user/selectPosition';
    dBlock.prefix = '/stompApi';
    this.ws.sendMessage(dBlock);
  }

  bridge(action: string, raise?: number){
    this.terminal.out('Action ['+action+'] - ' + raise, this.serviceName);
    const dI = new DecisionInform();
    dI.action = action;
    dI.ammount = raise;
    const dBlock = new MessageDefinition();
    dBlock.data = dI;
    dBlock.endpoint = '/game/bridge';
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
    this.ws.suscribe('/GameController/announcement', (data) => this.onAnnouncement(data)); // global message
    this.ws.suscribe('/userInterceptor/GameController/ingress', (data) => this.onIngress(data));
    this.ws.suscribe('/userInterceptor/GameController/rejectedPosition', (data) => this.onRejectedPosition(data));
    this.ws.suscribe('/userInterceptor/GameController/successDeposit', (data) => this.onSuccessDeposit(data));
    this.ws.suscribe('/userInterceptor/GameController/invalidDeposit', (data) => this.onInvalidDeposit(data));
    this.ws.suscribe('/GameController/startGame', (data) => this.onStartGame(data)); // global message
    this.ws.suscribe('/GameController/roundStart', (data) => this.onRoundStart(data)); // global message
    this.ws.suscribe('/GameController/blind', (data) => this.onBlind(data)); // global message
    this.ws.suscribe('/GameController/cardsDist', (data) => this.onCardsDist(data)); // global message
    this.ws.suscribe('/userInterceptor/GameController/cardsDist', (data) => this.onICardsDist(data));
    this.ws.suscribe('/GameController/actionFor', (data) => this.onActionFor(data)); // global message
    this.ws.suscribe('/userInterceptor/GameController/betDecision', (data) => this.onBetDecision(data));
    this.ws.suscribe('/GameController/flop', (data) => this.onFlop(data)); // global message
    this.ws.suscribe('/GameController/river', (data) => this.onRiver(data)); // global message
    this.ws.suscribe('/GameController/turn', (data) => this.onTurn(data)); // global message
  }

  onFlop(data) {
    this.terminal.note('FLOP: ' + JSON.stringify(data.cards));
  }

  onRiver(data) {
    this.terminal.note('River: ' + JSON.stringify(data.card));
  }

  onTurn(data) {
    this.terminal.note('Turn: ' + JSON.stringify(data.card));
  }

  onIngress(data) {
    this.terminal.info('Ingress Chips: '+data.chips+' - position: '+data.position);
  }

  onAnnouncement(data) {
    this.terminal.in('Announcement: Pos['+data.position+'] user['+data.user+'] chips['+data.chips+'] avatar['+data.avatar+']', this.serviceName);
  }

  onStartGame(data) {
    this.terminal.info('Start game in: ' + data.startIn + ' secs');
  }

  onRoundStart(data) {
    this.terminal.info('Starting round, Dealer: ' + data.dealerPosition + ' Round number: ' + data.roundNumber);
  }

  onBlind(data) {
    this.terminal.info('Small blind: {pos:' + data.sbPosition + ' $'+ data.sbChips + '} Big blind: {pos: ' + data.bbPosition + ' $'+data.bbChips+'}');
  }

  onCardsDist(data) {
    const cards = data.cards[1] ? '[][]' : '[]';
    this.terminal.log('Cards for ' + data.position + ' - ' + cards)
  }

  onICardsDist(data) {
    this.terminal.info('Cards for you ' + JSON.stringify(data.cards));
  }

  onActionFor(data) {
    this.terminal.log('Waiting '+data.position+' for: '+data.remainingTime+' seconds');
  }

  onBetDecision(data) {
    this.terminal.info('BetDecision: To Call: '+data.toCall+' can check? ' + (data.canCheck ? '[Yes]' : '{No}') + ' Raise >' + data.minRaise + ' and <' + data.maxRaise);
    this.actionButtonEvent.emit(data.toCall);
  }

  onRejectFullyfied(data) {
    this.terminal.err('REJECTED Fullyfied');
  }

  onDefinePosition(data) {
    this.terminal.in('Define Position, free positions: '+JSON.stringify(data.positions), this.serviceName);
  }

  onSuccessDeposit(data) {
    this.terminal.info('Success deposit: ' + data.chips);
  }

  onInvalidDeposit(data) {
    this.terminal.err('Invalid deposit.');
  }

  onDeposit(data) {
    this.terminal.in('Requesting deposit...', this.serviceName);
  }

  onRejectedPosition(data) {
    this.terminal.err('Rejected Position');
    this.terminal.in('Rejected, available positions: '+JSON.stringify(data.positions), this.serviceName);
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
