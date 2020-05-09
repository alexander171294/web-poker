import { Component, OnInit } from '@angular/core';
import { SoonModalService } from 'src/app/services/soon-modal.service';

@Component({
  selector: 'app-soon-modal',
  templateUrl: './soon-modal.component.html',
  styleUrls: ['./soon-modal.component.scss']
})
export class SoonModalComponent implements OnInit {

  public isShowing: boolean;

  constructor(private sms: SoonModalService) { }

  ngOnInit() {
    this.sms.getsME().subscribe(s => {
      if (s) {
        this.isShowing = true;
      }
    });
  }

}
