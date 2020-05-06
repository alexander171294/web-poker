import { InGameSchema } from './InGameSchema';

export class ChatMessage extends InGameSchema {
  public message: string;
    public constructor() {
        super('ChatMessage');
    }
}
