/*
 * Re-Implementación del stomp service por Alexander E.
 */

interface Config {
  // websocket endpoint
  host: string;
  // optional headers
  headers?: any;
  // heartbeats (ms)
  heartbeatIn?: number;
  heartbeatOut?: number;
  // debuging
  debug?: boolean;
  // reconnection time (ms)
  recTimeout?: number;
  // queue object
  queue: any;
  // queue cheking Time (ms)
  queueCheckTime?: number;
}
