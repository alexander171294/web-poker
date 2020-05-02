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
  @Input() toCall: number;
  @Input() actualBet: number = 0;
  @Output() raiseChanged: EventEmitter<number> = new EventEmitter<number>();

  raisePercent: string;
  actualRaise: number;

  constructor() { }

  ngOnInit() {
    // console.log('Initial raise: ', this.initialRaise);
    this.actualRaise = this.initialRaise;
    this.raisePercent = '0%';
  }

  changeDector(event: any) {
    this.actualRaise = event.target.valueAsNumber;
    let maxR;
    if (this.maxRaise > 0) {
      maxR = this.maxRaise + this.toCall > this.userChips ? this.userChips : this.maxRaise;
    } else {
      maxR = this.userChips;
    }
    if (this.actualRaise > maxR) {
      this.actualRaise = maxR;
    }
    const aR = this.actualRaise ? this.actualRaise : 0;
    event.target.value = aR;
    this.raiseChanged.emit(aR);
    const percent = Math.ceil(aR * 100 / maxR);
    this.raisePercent = percent + '%';
  }

  public refreshBet(bet: number) {
    this.actualRaise = bet;
    let maxR;
    if (this.maxRaise > 0) {
      maxR = this.maxRaise + this.toCall > this.userChips ? this.userChips : this.maxRaise;
    } else {
      maxR = this.userChips;
    }
    const percent = Math.ceil(bet * 100 / maxR);
    this.raisePercent = percent + '%';
  }

}
