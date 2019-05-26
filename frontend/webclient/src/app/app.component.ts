import { Component } from '@angular/core';
import { environment } from 'src/environments/environment';
import { SessionService } from './providers/session.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {

  title = 'webclient';
  version = environment.version;
  infoOpened: boolean = false;
  isLogged: boolean;

  constructor(private sessionSrv: SessionService) {
    sessionSrv.getLoginEvent().subscribe(logged => {
      this.isLogged = logged;
    });
  }

  logout() {
    this.sessionSrv.getLoginEvent().emit(false);
    // TODO: logout.
  }

}
