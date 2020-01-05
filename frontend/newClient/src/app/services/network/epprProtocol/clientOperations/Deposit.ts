import { ClientOperationsSchema } from '../ClientOperationsSchema';

export class Deposit extends ClientOperationsSchema {

    public chips: number;

    constructor() {
        super('deposit');
    }
}
