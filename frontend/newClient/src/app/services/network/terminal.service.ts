import { Injectable, EventEmitter } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class TerminalService {

  public event = new EventEmitter<string>();
  public errorEvent = new EventEmitter<string>();
  public infoEvent = new EventEmitter<string>();
  public noteEvent = new EventEmitter<string>();
  public debugEvents = new EventEmitter<string>();

  constructor() { }

  log(text:string) {
    this.event.emit('[*] ' + text);
  }

  err(text:string) {
    this.errorEvent.emit('[!] ' + text);
  }

  info(text:string) {
    this.infoEvent.emit('[?] ' + text);
  }

  note(text:string) {
    this.noteEvent.emit('[?] ' + text);
  }

  out(text: string, srv: string) {
    this.event.emit('@'+srv+' << ' + text);
  }

  in(text: string, srv: string) {
    this.event.emit('@'+srv+' >> ' + text);
  }

  dlog(text: string) {
    this.debugEvents.emit(' - - - - >' + text);
  }
}
