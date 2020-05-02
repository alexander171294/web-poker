import { environment } from './../../../../environments/environment';
import { Injectable, EventEmitter } from '@angular/core';
import { EventWS } from './EventWS';
import { EventTypeWS } from './EventTypeWS';
import { MessageDefinition } from './MessageDefinition';
import { StompService } from './stomp.service';

@Injectable({
  providedIn: 'root'
})
export class WebSocketService {

  protected server: string; // localhost
  protected port: number;
  protected endpoint: string;
  protected isConnected = false;
  public firstConnection = true;
  public isFullyRead = false;

  public wsEventSubscriptor: EventEmitter<EventWS> = new EventEmitter<EventWS>();

  constructor(private stomp: StompService) {
    this.stomp.onConnectEvent.subscribe((ev) => this.onConnect(ev));
    this.stomp.onErrorEvent.subscribe((ev) => this.onError(ev));
  }

  protected setBasicConfig(server: string, port: number, endpoint: string) {
    this.server = server;
    this.port = port;
    this.endpoint = endpoint;
    this.stomp.configure({
      host: 'http://' + server + ':' + port + endpoint, // "/external",
      debug: environment.stompDebug,
      queue: { init: false }
    });
    this.wsEventSubscriptor.emit(new EventWS(EventTypeWS.CONFIGURING));
  }

  protected startConnection() {
    this.wsEventSubscriptor.emit(new EventWS(EventTypeWS.CONNECTING, {host: 'http://' + this.server + ':' + this.port + this.endpoint}));
    this.stomp.startConnect().then(() => {
      this.stomp.done('init');
      this.isConnected = true;
      this.wsEventSubscriptor.emit(new EventWS(EventTypeWS.CONNECTED, {host: 'http://' + this.server + ':' + this.port + this.endpoint}));
    });
  }

  public sendMessage(dataBlock: MessageDefinition) {
    this.wsEventSubscriptor.emit(new EventWS(EventTypeWS.SENDING, dataBlock));
    this.stomp.send(dataBlock.getFullEndpoint(), dataBlock.data);
  }

  public suscribe(endpoint: string, callback: any, headers?: any) {
    this.wsEventSubscriptor.emit(new EventWS(EventTypeWS.SUSCRIPTION, endpoint));
    return this.stomp.subscribe(endpoint, (data) => {
      this.wsEventSubscriptor.emit(new EventWS(EventTypeWS.MESSAGE, {endpoint, data}));
      callback(data);
    }, headers);
  }

  public isConnectionActive(): boolean {
    return this.isConnected;
  }

  private onConnect(frame) {
    if (this.firstConnection) {
      this.wsEventSubscriptor.emit(new EventWS(EventTypeWS.FIRST_CONNECTION));
    } else {
      this.wsEventSubscriptor.emit(new EventWS(EventTypeWS.FULL_CONNECTION));
    }
  }

  private onError(err) {
    this.wsEventSubscriptor.emit(new EventWS(EventTypeWS.ERROR, err));
    this.isFullyRead = false;
  }

  public disconnect(callback?: any) {
    this.wsEventSubscriptor.emit(new EventWS(EventTypeWS.DISCONNECT));
    this.stomp.disconnect().then(() => {
      if (callback) {
        callback();
      }
      this.wsEventSubscriptor.emit(new EventWS(EventTypeWS.DISCONNECTED));
    })
  }

}
