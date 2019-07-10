import { EventTypeWS } from './EventTypeWS';

export class EventWS {
  public eventType: EventTypeWS;
  public data?: any;

  constructor(eventType: EventTypeWS, data?: any) {
    this.eventType = eventType;
    this.data = data;
  }
}
