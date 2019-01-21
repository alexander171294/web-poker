import { Injectable } from '@angular/core';
import { Server } from './servers/server';
import { StompService } from '../utils/stomp.service';
import { Settings } from '../../settings';
import { HttpClient } from '@angular/common/http';
import { httpPacker } from '../utils/headers';
import { Rooms } from './dto/Rooms';

@Injectable()
export class LobbyServer {

    constructor(private http: HttpClient) { }

    public getRoomsSitNGo() {
        const url = Settings.getUrl() + 'roomsRest/sitngo';
        return this.http.get<Rooms[]>(url, httpPacker.getHttpOptions());
    }

}
