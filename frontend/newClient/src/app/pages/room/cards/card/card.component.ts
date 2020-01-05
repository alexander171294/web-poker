import { Component, OnInit, Input } from '@angular/core';

@Component({
  selector: 'app-card',
  templateUrl: './card.component.html',
  styleUrls: ['./card.component.scss']
})
export class CardComponent implements OnInit {

  @Input() upsidedown: boolean;
  @Input() palo: number; // 0 corazones 1 diamantes 2 pica 3 trebol
  @Input() valor: number; // 2-3-4-5-6-7-8-9-10 11=j 12=Q 13=K 14/0=A

  constructor() { }

  ngOnInit() {
  }

}
