import { Component, OnInit, Input } from '@angular/core';
import { UserMenuControllerService } from '../user-menu-actions/user-menu-controller.service';

@Component({
  selector: 'app-room-vcard',
  templateUrl: './vcard.component.html',
  styleUrls: ['./vcard.component.scss']
})
export class VcardComponent implements OnInit {

  @Input() image: string;
  @Input() name: string;
  @Input() timeRest: number;
  @Input() chips: string;
  @Input() void: boolean;
  @Input() inGame: boolean;
  private actualTimeRest;
  public partGradesA = 180;
  public partGradesB = 180;
  public timmerActions: any;

  constructor() { }

  ngOnInit() {
  }

  startTimeRest(remainingTime: number) {
    this.timeRest = this.timeRest ? this.timeRest : remainingTime;
    this.actualTimeRest = this.timeRest * 10;
    this.timmerActions = setInterval(() => {
      this.actualTimeRest--;
      if (this.actualTimeRest < 0) {
        this.finishActions();
      } else {
        this.processPositions();
      }
    }, 100);
    this.processPositions();
  }

  private processPositions() {
    let grades = Math.floor(this.actualTimeRest * 360 / (this.timeRest * 10));
    if (grades > 180) {
      this.partGradesB = 0;
      grades = grades - 180;
      this.partGradesA = 180 - grades;
    } else {
      this.partGradesA = 180;
      this.partGradesB = 180 - grades;
    }
  }

  finishActions() {
    clearInterval(this.timmerActions);
    this.partGradesB = 180;
    this.partGradesA = 180;
  }

}
