import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Params } from '@angular/router';
import { RoomResponse } from 'src/app/services/roomsResponse';
import { RoomService } from 'src/app/services/network/room.service';
import { TerminalService } from 'src/app/services/network/terminal.service';
import { WsRoomService } from 'src/app/services/network/room/ws-room.service';
import { Authorization } from 'src/app/services/network/epprProtocol/userAuth/Authorization';
import { MessageDefinition } from 'src/app/services/network/utils/MessageDefinition';
import { LobbyService } from 'src/app/services/lobby.service';
import { BackwardValidation } from 'src/app/services/network/epprProtocol/userAuth/BackwardValidation';
import { ChallengeActions } from 'src/app/services/network/epprProtocol/userAuth/types/ChallengeActions';
import { Deposit } from 'src/app/services/network/epprProtocol/clientOperations/Deposit';
import { RxEType } from 'src/app/services/network/ReactionEvents';

@Component({
  selector: 'app-room',
  templateUrl: './room.component.html',
  styleUrls: ['./room.component.scss']
})
export class RoomComponent implements OnInit {

  public roomID: number;
  public roomData: RoomResponse;
  public connecting: string;
  public popupDepositOpened: boolean;
  public serverName: string;
  public isDeposit = false;
  public depositQuantity: number;

  constructor(private route: ActivatedRoute,
              private ws: WsRoomService,
              private room: RoomService,
              private terminal: TerminalService,
              private lobby: LobbyService) {
    terminal.event.subscribe(data => {
      console.log('[]> ' + data);
    });
    terminal.errorEvent.subscribe(data => {
      console.error('[]> ' + data);
    });
    terminal.infoEvent.subscribe(data => {
      console.warn('[]> ' + data);
    });
    terminal.noteEvent.subscribe(data => {
      console.log('!!! ' + data);
    });
    terminal.debugEvents.subscribe(data => {
      console.log('------------------> ' + data);
    });
    this.room.reactionEvent.subscribe(evt => {
      if (evt.type === RxEType.DEPOSIT_SUCCESS) {
        this.popupDepositOpened = false;
      }
    });
  }

  ngOnInit() {
    this.roomID = this.route.params['value'].id; // this.route.snapshot.queryParamMap.get('id');
    this.roomData = JSON.parse(sessionStorage.getItem('room-' + this.roomID));
    this.room.globalConnectionEvents.subscribe(data => {
      if (data === 2) { // connected
        this.connecting = 'Authorization request.';
        this.authorization(parseInt(localStorage.getItem('userID'),10));
      }
      if (data === 11) { // requesting claim token
        let obsrv = null;
        if (this.isDeposit) {
          obsrv = this.lobby.challengeD(this.room.roomID, this.room.authClaim, this.depositQuantity);
        } else {
          this.connecting = 'Challenge initialized.';
          obsrv = this.lobby.challenge(this.room.roomID, this.room.authClaim);
        }
        obsrv.subscribe(resp => {
          if (resp.operationSuccess) {
            if (!this.isDeposit) {
              this.connecting = 'Last validation.';
            }
            console.log('Backward validation for challenge: ', resp.challengeID);
            this.backwardValidation(resp.challengeID, this.isDeposit);
          } else {
            // TODO: trigger error popup.
            this.connecting = 'An error occurred, please re-login.';
          }
        }, err => {
          // TODO: trigger error popup.
          this.connecting = 'An error occurred, please re-login.';
        });
      }
      if (data === 12) { // Autenticado
        console.error('SETTED OFF');
        this.connecting = undefined;
      }
      if (data === 13) {
        this.connecting = 'An error occurred, please re-login.';
      }
      if (data === 14) {
        this.connecting = 'You are banned in this room :(.';
      }
      if (data === 15) {
        this.popupDepositOpened = true;
      }
    });
    this.connecting = 'Server connection.';
    this.room.connect(this.roomData.server_ip);
  }

  private authorization(userID: number) {
    console.warn('AUTHORIZING USER ', userID);
    const auth = new Authorization();
    auth.userID = userID;
    const dBlock = new MessageDefinition();
    dBlock.data = auth;
    dBlock.endpoint = '/user/authorization';
    dBlock.prefix = '/stompApi';
    this.ws.sendMessage(dBlock);
  }

  private backwardValidation(challengeID: number, deposit: boolean) {
    console.warn('BACKWARD VALIDATION');
    const bV = new BackwardValidation();
    bV.action = deposit ? ChallengeActions.DEPOSIT : ChallengeActions.LOGIN;
    bV.idChallenge = challengeID;
    const dBlock = new MessageDefinition();
    dBlock.data = bV;
    dBlock.endpoint = '/user/backwardValidation';
    dBlock.prefix = '/stompApi';
    this.ws.sendMessage(dBlock);
  }

  doActionNav(event: string) {
    if (event === 'deposit') {
      this.popupDepositOpened = true;
    }
  }

  doDeposit(quantity: number) {
    console.warn('Depositing...');
    this.isDeposit = true;
    this.depositQuantity = quantity;
    const deposit = new Deposit();
    deposit.chips = quantity;
    const dBlock = new MessageDefinition();
    dBlock.data = deposit;
    dBlock.endpoint = '/user/deposit';
    dBlock.prefix = '/stompApi';
    this.ws.sendMessage(dBlock);
  }

}
