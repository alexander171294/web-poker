import { Component, ViewChildren } from '@angular/core';
import { RoomService } from './services/room.service';
import { TerminalService } from './services/terminal.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  title = 'cli-toolkit';

  private roomServer = '127.0.0.1:8081';
  private orchestratorServer = '127.0.0.1:8082';
  private apiServer = '127.0.0.1:8083';
  private commandPrompt: string;


  private terminalMessages = [
    'Ready'
  ];

  constructor(private room: RoomService, private terminal: TerminalService) {
    terminal.event.subscribe(data => {
      this.terminalMessages.push(data);
    });
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
  ingressCMD() {
    this.commandPrompt = 'room.ingress user:"NickName" photo:"PhotoURL" chips:"4500"';
    document.getElementById('commandPrompt').focus();
  }

  depositCMD() {
    this.commandPrompt = 'room.deposit user:"57" coins:"500" challengeID:"16" claimToken:"xMjM0NTY3ODkwIiwibmFtZSI6Ikpva"';
    document.getElementById('commandPrompt').focus();
  }

  roomChallengeCMD() {
    this.commandPrompt = 'apisrv.challenge challengeID:"16" claimToken:"xMjM0NTY3ODkwIiwibmFtZSI6Ikpva"';
    document.getElementById('commandPrompt').focus();
  }

  backwardValidationCMD() {
    this.commandPrompt = 'room.backwardValidation challengeID:"16"';
    document.getElementById('commandPrompt').focus();
  }

  processCommand(command: string) {
    const part = /([a-zA-Z]+)\.([a-zA-Z]+)/gm.exec(command);
    const target = part[1];
    const action = part[2];
    const params = /([a-zA-Z]+:"[a-zA-Z0-9]+")/gm.exec(command);
    console.log(target, action, params);
  }
}
