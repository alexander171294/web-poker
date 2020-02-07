import { UserProfile } from './../../services/UserProfile';
import { UsersService } from './../../services/users.service';
import { Component, OnInit, OnDestroy } from '@angular/core';
import { environment } from 'src/environments/environment';
import { LobbyService } from 'src/app/services/lobby.service';
import { RoomResponse } from 'src/app/services/roomsResponse';
import { Security } from 'src/app/services/Security';

@Component({
  selector: 'app-lobby',
  templateUrl: './lobby.component.html',
  styleUrls: ['./lobby.component.scss']
})
export class LobbyComponent implements OnInit, OnDestroy {

  public version = environment.version;
  private rooms: RoomResponse[];
  public loadingProfileData = true;
  public userData: UserProfile = new UserProfile();
  public intUserData: any;

  constructor(private lobbySrv: LobbyService, private userSrv: UsersService) { }

  ngOnInit() {
    this.updateRooms();
    this.loadingProfileData = true;
    this.getProfileData();
    this.intUserData = setInterval(() => this.getProfileData(), 1500);
  }

  updateRooms(callback?) {
    this.lobbySrv.getRooms().subscribe(rooms => {
      this.rooms = rooms.rooms;
      if (callback) {
        callback();
      }
    });
  }

  getProfileData() {
    this.userSrv.getUser(Security.getJWTData().iss).subscribe(data => {
      this.loadingProfileData = false;
      this.userData = data;
      console.log(data);
    });
  }

  ngOnDestroy(): void {
    clearInterval(this.intUserData);
  }

}
