import { Component, OnInit, Output, EventEmitter, Input } from '@angular/core';
import { environment } from 'src/environments/environment';

@Component({
  selector: 'app-nav-bar',
  templateUrl: './nav-bar.component.html',
  styleUrls: ['./nav-bar.component.scss']
})
export class NavBarComponent implements OnInit {

  @Output() action: EventEmitter<string> = new EventEmitter<string>();
  @Input() serverName: string;
  @Input() description: string;

  public version = environment.version;

  constructor() { }

  ngOnInit() {
  }

}
