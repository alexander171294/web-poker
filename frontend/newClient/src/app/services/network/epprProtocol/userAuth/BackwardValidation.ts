import { UserAuthSchema } from '../UserAuthSchema';
import { ChallengeActions } from './types/ChallengeActions';

export class BackwardValidation extends UserAuthSchema {

    public idChallenge: number;
    public action: ChallengeActions;

    constructor() {
        super('backwardValidation');
    }

}