import { Component } from '@angular/core';
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
}
