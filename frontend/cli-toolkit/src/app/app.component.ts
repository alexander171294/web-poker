import { Component, ViewChildren } from '@angular/core';
import { RoomService } from './services/room.service';
import { TerminalService } from './services/terminal.service';
import { ApiService } from './services/api.service';
import { environment } from 'src/environments/environment';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  title = 'cli-toolkit';

  private roomServer = '127.0.0.1:8081';
  private orchestratorServer = '127.0.0.1:8082';
  private apiServer = 'http://127.0.0.1:8083';
  private commandPrompt: string;
  private debugMode: boolean;

  callSize: number;
  check: boolean = false;
  actionButtons: boolean = false;


  private terminalMessages = [
    {msg: 'Ready', type: 'info'}
  ];

  constructor(private room: RoomService, private terminal: TerminalService, private api: ApiService) {
    room.actionButtonEvent.subscribe(data => {
      this.actionButtons = true;
      this.check = data === 0;
      this.callSize = data;
    });
    terminal.event.subscribe(data => {
      this.terminalMessages.push({
        msg: data,
        type: 'normal'
      });
      this.goToBottom();
    });
    terminal.errorEvent.subscribe(data => {
      this.terminalMessages.push({
        msg: data,
        type: 'error'
      });
      this.goToBottom();
    });
    terminal.infoEvent.subscribe(data => {
      this.terminalMessages.push({
        msg: data,
        type: 'info'
      });
      this.goToBottom();
    });
    this.debugMode = environment.debugMode;
    if(environment.debugMode) {
      terminal.debugEvents.subscribe(data => {
        this.terminalMessages.push({
          msg: data,
          type: 'debug'
        });
        this.goToBottom();
      });
    }
  }

  connectRoomServer() {
    this.room.connect(this.roomServer);
  }

  keyUp(evt) {
    if(evt.keyCode === 13){
      // eval command.
      this.processCommand(this.commandPrompt);
      this.commandPrompt = '';
    }
  }

  // Command writter helper // 
  authCMD() {
    this.commandPrompt = 'room.authorization userID:"1"';
    document.getElementById('commandPrompt').focus();
  }

  depositCMD() {
    this.commandPrompt = 'room.deposit chips:"500"';
    document.getElementById('commandPrompt').focus();
  }

  roomChallengeCMD() {
    this.commandPrompt = 'apisrv.challenge roomID:"15" claimToken:"xMjM0NTY3ODkwIiwibmFtZSI6Ikpva"';
    document.getElementById('commandPrompt').focus();
  }

  depositChallengeCMD() {
    this.commandPrompt = 'apisrv.depositChallenge roomID:"15" claimToken:"xMjM0NTY3ODkwIiwibmFtZSI6Ikpva" chips:"500"';
    document.getElementById('commandPrompt').focus();
  }

  backwardValidationCMD() {
    this.commandPrompt = 'room.backwardValidation challengeID:"16"';
    document.getElementById('commandPrompt').focus();
  }

  backwardValidationDCMD() {
    this.commandPrompt = 'room.backwardValidation challengeID:"16" deposit:"true"';
    document.getElementById('commandPrompt').focus();
  }

  sitCMD() {
    this.commandPrompt = 'room.sit position:"2"';
    document.getElementById('commandPrompt').focus();
  }

  foldCommand() {
    this.commandPrompt = 'game.bridge action:"fold"';
    document.getElementById('commandPrompt').focus();
  }

  callCommand() {
    this.commandPrompt = 'game.bridge action:"call"';
    document.getElementById('commandPrompt').focus();
  }

  checkCommand() {
    this.commandPrompt = 'game.bridge action:"check"';
    document.getElementById('commandPrompt').focus();
  }

  raiseCommand() {
    this.commandPrompt = 'game.bridge action:"raise" raise:"100"';
    document.getElementById('commandPrompt').focus();
  }

  processCommand(command: string) {
    const part = /([a-zA-Z]+)\.([a-zA-Z]+)/gm.exec(command);
    const target = part[1];
    const action = part[2];
    var params: any = {};
    command.match(/[a-zA-Z]+:"[a-zA-Z0-9-]+"/gm).forEach(data => {
      const result = /([a-zA-Z]+):"([a-zA-Z0-9-]+)"/gm.exec(data);
      params[result[1]] = result[2];
    });
    console.log(target, action, params);
    if(target == 'room') {
      if(action == 'authorization') {
        this.room.authorization(params.userID);
      }
      if(action == 'ingress') {
        this.room.ingress(params.user, params.photo);
      }
      if(action == 'deposit') {
        this.room.deposit(params.chips);
      }
      if(action == 'backwardValidation') {
        this.room.backwardValidation(params.challengeID, params.deposit === 'true');
      }
      if(action == 'sit') {
        this.room.selectPosition(params.position);
      }
    }
    if(target == 'apisrv') {
      if(action == 'challenge') {
        // mock user challenge for test (using the first user with id = 1 hardcoded for test purposes)
        this.terminal.out('Challenge Room','ApiSrv');
        this.api.setEndpoint(this.apiServer);
        this.api.challenge(params.roomID, params.claimToken).subscribe(data => {
          this.terminal.in('Challenge Response OK, challengeID: ' + data.challengeID ,'ApiSrv')
        }, err => {
          this.terminal.in('Challenge refused','ApiSrv');
        });
      }
      if(action == 'depositChallenge') {
        // mock user challenge for test (using the first user with id = 1 hardcoded for test purposes)
        this.terminal.out('Challenge Deposit','ApiSrv');
        this.api.setEndpoint(this.apiServer);
        this.api.challengeD(params.roomID, params.claimToken, params.chips).subscribe(data => {
          this.terminal.in('Challenge Response OK, challengeID: ' + data.challengeID ,'ApiSrv')
        }, err => {
          this.terminal.in('Challenge refused','ApiSrv');
        });
      }
    }
    if(target == 'game') {
      if(action == 'bridge') {
        if(params.action == 'raise') {
          this.room.bridge(params.action, params.raise);
        } else {
          this.room.bridge(params.action);
        }
      }
    }
  }

  goToBottom() {
    setTimeout(() => {
      const objDiv = document.getElementById('eventsTerminal');
      objDiv.scrollTop = objDiv.scrollHeight;
    }, 100);
  }
}
