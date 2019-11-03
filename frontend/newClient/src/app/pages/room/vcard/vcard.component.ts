import { Component, OnInit, Input } from '@angular/core';

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

  constructor() { }

  ngOnInit() {
  }

}
