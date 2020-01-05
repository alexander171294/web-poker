export class ChatMessageData {
  public isSystem: boolean;
  public player: string;
  public message: string;

  constructor(isSystem: boolean, message: string, player?: string) {
    this.isSystem = isSystem;
    this.message = message;
    this.player = player;
  }
}
