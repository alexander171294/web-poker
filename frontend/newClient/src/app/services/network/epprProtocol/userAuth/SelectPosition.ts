import { UserAuthSchema } from '../UserAuthSchema';

export class SelectPosition extends UserAuthSchema {

    public position: number;

    constructor() {
        super('selectPosition');
    }
}
