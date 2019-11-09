import { Schema } from './Schema';

export class UserAuthSchema extends Schema {

    constructor(schema: string) {
        super();
        this.namespace = 'eppr/user-auth';
        this.schema = schema;
    }

}