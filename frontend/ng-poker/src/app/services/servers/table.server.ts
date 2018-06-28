import { Injectable } from "@angular/core";
import { Server } from "./server";
import { StompService } from "../../utils/stomp.service";
import { Settings } from "../../../settings";

export class TableServer extends Server {

    public connect(server: string, port: number){
        this.server = server;
        this.port = port;
        this._connect();
    }

}