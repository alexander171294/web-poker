import { InGameSchema } from './InGameSchema';

export class Pots extends InGameSchema {
  public pots: number[];
  public constructor() {
    super("Pots");
  }
}
