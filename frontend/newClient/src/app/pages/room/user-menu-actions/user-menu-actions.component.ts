import { Component, OnInit } from '@angular/core';
import { UserMenuControllerService } from './user-menu-controller.service';
import { FriendsService } from 'src/app/services/friends.service';
import { Security } from 'src/app/services/Security';
import { SoonModalService } from 'src/app/services/soon-modal.service';

@Component({
  selector: 'app-user-menu-actions',
  templateUrl: './user-menu-actions.component.html',
  styleUrls: ['./user-menu-actions.component.scss']
})
export class UserMenuActionsComponent implements OnInit {

  public isShowing: boolean;
  public x: number;
  public y: number;
  public uid: number;

  public followLoader: boolean;
  public unfollowLoader: boolean;
  public viewProfileLoader: boolean;
  public notesLoader: boolean;
  public actionOnMe: boolean;

  public nowFollowing: boolean;

  constructor(private umc: UserMenuControllerService, private fs: FriendsService, private sms: SoonModalService) {
    this.umc.getUME().subscribe(s => {
      if (s) {
        this.isShowing = true;
        this.x = s.x + 15;
        this.y = s.y + 15;
        this.uid = s.uid;
        this.actionOnMe = Security.getJWTData().iss == s.uid;
        console.log(Security.getJWTData().iss, parseInt(s.uid, 10), Security.getJWTData().iss === s.uid);
        if (!this.actionOnMe) {
          this.fs.getFriendshipStatus(this.uid).subscribe(d => {
            this.followLoader = false;
            if (d.operationSuccess) {
              this.nowFollowing = d.status;
            }
          });
          this.followLoader = true;
        }
        this.nowFollowing = false;
      }
    });
  }

  ngOnInit() {
  }

  follow() {
    this.followLoader = true;
    this.fs.sendFriendsRequest(this.uid).subscribe(d => {
      if (d.operationSuccess) {
        this.nowFollowing = true;
      }
      this.followLoader = false;
    }, e => {
      this.followLoader = false;
      alert('An error was occurred, please try again later.');
    });
  }

  unfollow() {
    this.unfollowLoader = true;
    this.fs.removeFriendship(this.uid).subscribe(d => {
      if (d.operationSuccess) {
        this.nowFollowing = false;
      }
      this.unfollowLoader = false;
    }, e => {
      this.unfollowLoader = false;
      alert('An error was occurred, please try again later.');
    });
  }

  viewProfile() {

  }

  viewNotes() {

  }

  soon(){
    this.sms.show();
  }

}
