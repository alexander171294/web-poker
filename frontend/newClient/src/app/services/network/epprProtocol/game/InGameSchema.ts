import { SchemaTexasHoldem } from './SchemaTexasHoldem';

export class InGameSchema extends SchemaTexasHoldem {

    public constructor(schema: string) {
        super();
        this.namespace = 'inGame';
        this.schema = schema;
    }

}