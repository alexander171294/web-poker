import { RoomService } from './../../../services/network/room.service';
import { Router } from '@angular/router';
import { Component, OnInit, Output, EventEmitter, Input } from '@angular/core';
import { environment } from 'src/environments/environment';

@Component({
  selector: 'app-nav-bar',
  templateUrl: './nav-bar.component.html',
  styleUrls: ['./nav-bar.component.scss']
})
export class NavBarComponent implements OnInit {

  @Output() action: EventEmitter<string> = new EventEmitter<string>();
  @Input() serverName: string;
  @Input() description: string;

  public version = environment.version;

  constructor(private router: Router, private room: RoomService) { }

  ngOnInit() {
  }

  backToLobby() {
    this.router.navigate(['lobby']);
  }

  leave() {
    // proceso de recuperación de fichas.
    this.room.leave();
    // TODO: uncomment me when have timmer fold:
    // window.close();
  }

}
