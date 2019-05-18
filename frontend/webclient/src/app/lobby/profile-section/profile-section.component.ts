import { Component, OnInit } from '@angular/core';
import { SessionService } from 'src/app/providers/session.service';
import { UsersService } from 'src/app/providers/users/users.service';

@Component({
  selector: 'app-profile-section',
  templateUrl: './profile-section.component.html',
  styleUrls: ['./profile-section.component.scss']
})
export class ProfileSectionComponent implements OnInit {

  constructor(private session: SessionService, private userSrv: UsersService) { }

  ngOnInit() {

  }

}
