import { InGameSchema } from './InGameSchema';

export class DecisionInform extends InGameSchema {
    public action: string;
    public ammount?: number;
    public constructor() {
        super("decisionInform");
    }
}