import { GeneralResponse } from './GeneralResponse';

export class SessionInformation extends GeneralResponse {
    public userID: string;
    public jwtToken: string;
    public sessionID: string;
}
