export class UserProfile {
  public idUser: number;
  public nick: string;
  public photo: string;
  public chips: number;
}

export class FriendCard {
  public idUser: number;
  public nick: string;
  public photo: string;
  public inGame: boolean;
  public rooms: any[];
  public status: string;
}
