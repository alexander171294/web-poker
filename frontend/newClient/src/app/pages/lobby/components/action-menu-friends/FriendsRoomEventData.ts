import { Room } from 'src/app/services/roomsResponse';

export class FriendsRoomEventData {
    public x: number;
    public y: number;
    public rooms: Room[];
    
    constructor(x: number, y: number, rooms: Room[]) {
      this.x = x;
      this.y = y;
      this.rooms = rooms;
    }
  }
  