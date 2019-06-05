import { Component, OnInit, EventEmitter, Output, Input } from '@angular/core';

@Component({
  selector: 'app-box-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit {

  @Output() info: EventEmitter<void> = new EventEmitter<void>();
  @Input() isLogged: boolean;

  constructor() { }

  ngOnInit() {
  }

}
