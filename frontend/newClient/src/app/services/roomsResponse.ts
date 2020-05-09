import { GeneralResponse } from './GeneralResponse';

export class RoomsResponse extends GeneralResponse {
    public rooms: RoomResponse[];
}

export class RoomResponse extends GeneralResponse {

    public id_room: number;
    public server_ip: string;
    public name: string;
    public gproto: string;
    public description: string;
    public max_players: number;
    public minCoinForAccess: number;
    public isOfficial: boolean;
    public players: number;
}

export class Room {
    id_room: number
    id_user: number
    position: number
    registered: string
}
