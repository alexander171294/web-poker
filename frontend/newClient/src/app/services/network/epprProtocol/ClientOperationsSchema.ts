import { Schema } from './Schema';

export class ClientOperationsSchema extends Schema {

    constructor(schema: string) {
        super();
        this.namespace = 'eppr/client-operations';
        this.schema = schema;
    }

}