import { Component, OnInit, Input } from '@angular/core';

@Component({
  selector: 'app-connecting',
  templateUrl: './connecting.component.html',
  styleUrls: ['./connecting.component.scss']
})
export class ConnectingComponent implements OnInit {

  @Input() detail: string;

  constructor() { }

  ngOnInit() {
  }

}
