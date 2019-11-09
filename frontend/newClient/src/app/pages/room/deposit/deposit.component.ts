import { Component, OnInit, Output, EventEmitter, Input } from '@angular/core';

@Component({
  selector: 'app-deposit',
  templateUrl: './deposit.component.html',
  styleUrls: ['./deposit.component.scss']
})
export class DepositComponent implements OnInit {

  @Output() deposit: EventEmitter<number> = new EventEmitter<number>();
  @Output() cancel: EventEmitter<void> = new EventEmitter<void>();
  chips: number = 500;
  @Input() minDeposit: number;

  constructor() { }

  ngOnInit() {
  }

}
