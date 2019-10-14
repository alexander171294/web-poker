import { Injectable, EventEmitter } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class TerminalService {

  public event = new EventEmitter<string>();

  constructor() { }

  log(text:string) {
    this.event.emit('[*] ' + text);
  }

  err(text:string) {
    this.event.emit('[!] ' + text);
  }

  info(text:string) {
    this.event.emit('[?] ' + text);
  }

  dlog(text: string) {
    this.event.emit(' - - - - >' + text);
  }
}
