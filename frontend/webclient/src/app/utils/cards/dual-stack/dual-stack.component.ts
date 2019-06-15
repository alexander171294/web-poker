import { Component, OnInit, Input } from '@angular/core';
import { Card } from './Card';

@Component({
  selector: 'app-dual-stack',
  templateUrl: './dual-stack.component.html',
  styleUrls: ['./dual-stack.component.scss']
})
export class DualStackComponent implements OnInit {

  @Input() upsidedown: boolean;
  @Input() cards: Card[];

  constructor() { }

  ngOnInit() {
  }

}
