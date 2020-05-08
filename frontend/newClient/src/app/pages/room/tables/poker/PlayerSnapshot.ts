import { Card } from '../../cards/dual-stack/Card';

export class PlayerSnapshot {
    public playerDetails: PlayerDetails;
    public actualBet: number;
    public cards: Card[];
    public upsidedown: boolean;

    // frontend status variables:
    public timeRest?: number;
    public winner?: boolean;
    public inGame: boolean;

    public constructor() {
        this.playerDetails = new PlayerDetails();
        this.cards = [];
        this.upsidedown = true;
        this.actualBet = 0;
    }
}

export class PlayerDetails {
    public image: string;
    public name: string;
    public chips: number;
    public userID?: number;
}
