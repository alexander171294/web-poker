import { Component, OnInit, EventEmitter, Output, Input } from '@angular/core';
import { SessionService } from 'src/app/providers/session.service';
import { environment } from 'src/environments/environment';

@Component({
  selector: 'app-box-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit {

  @Output() info: EventEmitter<void> = new EventEmitter<void>();
  @Input() isLogged: boolean;
  version = environment.version;

  constructor(private sessionSrv: SessionService) { }

  ngOnInit() {
  }

  logout() {
    this.sessionSrv.getLoginEvent().emit(false);
    // TODO: logout.
  }

}
