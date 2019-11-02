import { Component, OnInit, Input } from '@angular/core';

@Component({
  selector: 'app-vcard',
  templateUrl: './vcard.component.html',
  styleUrls: ['./vcard.component.scss']
})
export class VcardComponent implements OnInit {

  @Input() status: string = '';
  @Input() spectateButton: boolean;
  @Input() statusColor;
  @Input() playedHours: number;
  @Input() nick: string;

  constructor() { }

  ngOnInit() {
  }

}
