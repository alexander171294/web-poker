import { UserProfile, FriendCard } from './../../services/UserProfile';
import { UsersService } from './../../services/users.service';
import { Component, OnInit, OnDestroy } from '@angular/core';
import { environment } from 'src/environments/environment';
import { LobbyService } from 'src/app/services/lobby.service';
import { RoomResponse } from 'src/app/services/roomsResponse';
import { Security } from 'src/app/services/Security';
import { FriendsService } from 'src/app/services/friends.service';

@Component({
  selector: 'app-lobby',
  templateUrl: './lobby.component.html',
  styleUrls: ['./lobby.component.scss']
})
export class LobbyComponent implements OnInit, OnDestroy {

  public version = environment.version;
  public rooms: RoomResponse[];
  public loadingProfileData = true;
  public userData: UserProfile = new UserProfile();
  public intUserData: any;
  public friendList: FriendCard[] = [];

  constructor(private lobbySrv: LobbyService, private userSrv: UsersService, private friendsService: FriendsService) { }

  ngOnInit() {
    this.updateRooms();
    this.loadingProfileData = true;
    this.getProfileData();
    this.intUserData = setInterval(() => this.getProfileData(), environment.refreshProfileTime);
    this.gerFriendList();
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
      // console.log(data);
    });
  }

  gerFriendList() {
    this.friendsService.getFriends().subscribe(data => {
      data.friends.forEach(element => {
        const aux: FriendCard = {
          idUser: element.idUser,
          nick: element.nick,
          photo: element.photo,
          inGame: element.inGame,
          rooms: element.rooms,
          status: element.inGame ? 'In Game' : 'Online'
        }
      this.friendList.push(aux);
      });
      console.log(data);
    });
  }

  ngOnDestroy(): void {
    clearInterval(this.intUserData);
  }

}
