import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";


export interface VpnClient {
  cn: string;
  vpnIp: string;
  allowedDestinations: string[];
}

@Injectable({
  providedIn: 'root'
})
export class VpnClientService {

  private baseUrl = 'http://localhost:8080/api/v1/clients';

  constructor(private http: HttpClient) {}


  getAllClients(): Observable<VpnClient[]> {
    return this.http.get<VpnClient[]>(`${this.baseUrl}`);
  }


  getClientByCn(cn: string): Observable<VpnClient> {
    return this.http.get<VpnClient>(`${this.baseUrl}/${cn}`);
  }

  getIpRanges() {
    return this.http.get<any[]>(`${this.baseUrl}/ranges`);
  }
}
