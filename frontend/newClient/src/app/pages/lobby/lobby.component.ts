import { UserProfile, FriendCard, FriendData } from './../../services/UserProfile';
import { UsersService } from './../../services/users.service';
import { Component, OnInit, OnDestroy } from '@angular/core';
import { environment } from 'src/environments/environment';
import { LobbyService } from 'src/app/services/lobby.service';
import { RoomResponse } from 'src/app/services/roomsResponse';
import { Security } from 'src/app/services/Security';
import { FriendsService } from 'src/app/services/friends.service';
import { SoonModalService } from 'src/app/services/soon-modal.service';

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

  constructor(private lobbySrv: LobbyService, private userSrv: UsersService, private friendsService: FriendsService, private sms: SoonModalService) { }

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
      console.log(rooms);
      
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
    this.friendsService.getFriends().subscribe((data: FriendData) => {
      data.friends.forEach(element => {
        const aux: FriendCard = {
          idUser: element.idUser,
          nick: element.nick,
          photo: element.photo,
          inGame: element.inGame,
          rooms: element.rooms,
          status: element.inGame ? 'In Game' : 'Not playing'
        }
      this.friendList.push(aux);
      });
    });
  }

  ngOnDestroy(): void {
    clearInterval(this.intUserData);
  }

  soon(){
     this.sms.show();
  }

}
