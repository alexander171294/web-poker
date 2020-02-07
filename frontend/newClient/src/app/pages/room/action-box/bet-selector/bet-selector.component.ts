import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';

@Component({
  selector: 'app-bet-selector',
  templateUrl: './bet-selector.component.html',
  styleUrls: ['./bet-selector.component.scss']
})
export class BetSelectorComponent implements OnInit {

  @Input() maxRaise: number;
  @Input() initialRaise: number;
  @Input() userChips: number;
  @Output() raiseChanged: EventEmitter<number> = new EventEmitter<number>();

  raisePercent: string;
  actualRaise: number;

  constructor() { }

  ngOnInit() {
    console.log('Initial raise: ', this.initialRaise);
    this.actualRaise = this.initialRaise;
    this.raisePercent = '0%';
  }

  changeDector(event: any) {
    const maxR = this.maxRaise > 0 ? this.maxRaise : 500; // this.userChips;
    if (this.actualRaise > maxR) {
      this.actualRaise = maxR;
    }
    const aR = this.actualRaise ? this.actualRaise : 0;
    console.log(event, aR, maxR);
    this.raiseChanged.emit(aR);
    const percent = Math.ceil(aR * 100 / maxR);
    this.raisePercent = percent + '%';
  }

}
