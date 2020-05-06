import { InGameSchema } from './InGameSchema';

export class SnapshotRequest extends InGameSchema {
    public round: number;
    public constructor() {
        super('SnapshotRequest');
    }
}
