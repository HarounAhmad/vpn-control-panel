export class ClientStatus {
  cn: string = '';
  realIp: string = '';
  vpnIp: string = '';
  bytesIn: number = 0;
  bytesOut: number = 0;
  connectedSince: Date = new Date();
  status: string = '';
  lastSeen: Date = new Date();
}
