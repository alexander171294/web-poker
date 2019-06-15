import { Component, OnInit } from '@angular/core';
import { SessionService } from '../providers/session.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-lobby',
  templateUrl: './lobby.component.html',
  styleUrls: ['./lobby.component.scss', '../utils/box/main.box.scss']
})
export class LobbyComponent implements OnInit {

  infoOpened = false;
  isLogged: boolean;

  constructor(
    private session: SessionService,
    private router: Router,
    private sessionSrv: SessionService) {
    sessionSrv.getLoginEvent().subscribe(logged => {
      this.isLogged = logged;
    });
  }

  ngOnInit() {
    if (!this.session.isSessionActive()) {
      this.router.navigate(['/login']);
    }
  }

}
