import { Component } from '@angular/core';
import { RoomService } from './services/room.service';

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

  constructor(private room: RoomService) {
    
  }

  connectRoomServer() {
    this.room.connect(this.roomServer);
  }
}
