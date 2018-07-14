export class Response {
    public statusCode: StatusCodes = StatusCodes.OK;
    public message: string;
}

export enum StatusCodes {
    OK = 'OK',
    ERR = 'ERR'
}
