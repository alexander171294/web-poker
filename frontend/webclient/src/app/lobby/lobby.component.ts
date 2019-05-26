import { Component, OnInit } from '@angular/core';
import { SessionService } from '../providers/session.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-lobby',
  templateUrl: './lobby.component.html',
  styleUrls: ['./lobby.component.scss']
})
export class LobbyComponent implements OnInit {

  constructor(private session: SessionService, private router: Router) { }

  ngOnInit() {
    if(!this.session.isSessionActive()) {
      this.router.navigate(['/login']);
    }
  }

}
