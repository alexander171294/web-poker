import { Injectable } from "@angular/core";
import { Observable } from 'rxjs';
import { Settings } from "../../../settings";
import { StompService } from "../../utils/stomp.service";
import { MessageDefinition } from "../../utils/message-definition";

export class Server{
    protected server: string = Settings.LOBBY_HOST; // localhost
    protected port: number = Settings.LOBBY_PORT;
    protected isConnected: boolean;
    
    constructor(private stomp: StompService){
        console.log('Lobby starting -- Configuring');
        this.configure();
        this.stomp.onConnectEvent.subscribe((ev) => this.onConnected(ev));
        this.stomp.onErrorEvent.subscribe((ev) => this.onError(ev));
    }

    protected configure(){
        this.stomp.configure({
            host:"http://"+this.server+":"+this.port+"/external",
            debug:true,
            queue:{'init':false}
        });
    }

    protected _connect(){
        this.stomp.startConnect().then(() => {
            this.stomp.done('init');
            console.log('Conectado...');
            this.isConnected = true;
        });
    }

    protected onConnected(event){
        console.log("<< CONNECTION HANDLING OK");
    }

    protected onError(event){
        console.log('Lobby is connection closed.');
    }

    public disconnect(callback: any){
        console.log("!! Forzado desconexion disconnect(callback)");
        this.stomp.disconnect().then(() => {
          callback();
        })
    }

    public sendMessage(dataBlock: MessageDefinition){
        console.log(">> Se envia mensaje "+dataBlock.getFullEndpoint());
        this.stomp.send(dataBlock.getFullEndpoint(), dataBlock.data);
    }

    public subscribe(endpoint: string, callback: any, headers?: any){
        console.log(">> Suscripcion a endpoint: "+endpoint);
        return this.stomp.subscribe(endpoint, callback, headers);
    }

    public isConnectionActive():boolean{
        return this.isConnected;
    }
}