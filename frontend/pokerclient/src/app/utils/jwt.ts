import * as crypto from 'crypto-js';

export class JWToken {
  public header: JWTHead = new JWTHead();
  public payload: any;
  public signature: string;

  public pack(secret: string): string {
    this.signature = this.sign(window.btoa(JSON.stringify(this.header)), window.btoa(JSON.stringify(this.payload)), secret);
    return window.btoa(JSON.stringify(this.header)) + '.' + window.btoa(JSON.stringify(this.payload)) + '.' + this.signature;
  }

  private sign(head: string, payload: string, secret: string) {
    return crypto.enc.Base64.stringify(crypto.HmacSHA256(head + '.' + payload, secret));
  }
}

export class JWTHead {
  public alg = 'HS256';
  public typ = 'JWT';
}
