import { UserAuthSchema } from '../UserAuthSchema';

export class Authorization extends UserAuthSchema {

    public userID: number;

    constructor() {
        super('authorization');
    }

}
