export class Settings {

    public static LOBBY_HOST = 'localhost';
    public static LOBBY_PORT = 7265;
    public static VERSION = '1.0.0';

    public static getUrl(): string {
        return 'http://' + Settings.LOBBY_HOST + ':' + Settings.LOBBY_PORT + '/';
    }

}
