export class Response {
    public statusCode: StatusCodes = StatusCodes.OK;
    public message: string;
    public upgrade: string;
}

export enum StatusCodes {
    OK = 'OK',
    ERR = 'ERR'
}
