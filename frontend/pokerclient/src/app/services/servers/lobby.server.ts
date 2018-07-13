import { Injectable } from '@angular/core';
import { Server } from './server';
import { StompService } from '../../utils/stomp.service';
import { Settings } from '../../../settings';

@Injectable()
export class LobbyServer extends Server {

    constructor(stomp: StompService) {
        super(stomp);
        this._connect();
    }

}
