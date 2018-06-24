export class MessageDefinition {
    public prefix: string = "/api";
    public endpoint: string;
    public data: any;
    
    public getFullEndpoint(){
      return this.prefix+this.endpoint;
    }
  }