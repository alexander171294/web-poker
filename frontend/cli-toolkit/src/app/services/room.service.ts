import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class RoomService {

  constructor() { }

  public connect(serverIP: string) {
    alert(serverIP);
  }
}
