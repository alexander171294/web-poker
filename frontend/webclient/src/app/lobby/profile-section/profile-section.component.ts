import { Component, OnInit } from '@angular/core';
import { SessionService } from 'src/app/providers/session.service';
import { UsersService } from 'src/app/providers/users/users.service';
import { UserProfile } from 'src/app/providers/users/userProfile';

@Component({
  selector: 'app-profile-section',
  templateUrl: './profile-section.component.html',
  styleUrls: ['./profile-section.component.scss']
})
export class ProfileSectionComponent implements OnInit {

  private userData: UserProfile;

  constructor(private session: SessionService, private userSrv: UsersService) { }

  ngOnInit() {
    this.userSrv.getProfile(this.session.getSessionInfo().userID).subscribe(
      data => this.onInfoUpdated(data),
      err => {
        // TODO: improve this alert:
        alert('Connection error.');
      }
    );
  }

  onInfoUpdated(data: UserProfile) {
    this.userData = data;
  }

}
