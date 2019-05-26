import { GeneralResponse } from '../GeneralResponse';

export class SessionInformation extends GeneralResponse {
    public userID: number;
    public jwtPasspharse: string;
    public sessionID: number;
}