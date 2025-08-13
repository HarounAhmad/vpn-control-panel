import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";


export interface VpnClient {
  cn: string;
  vpnIp: string;
  allowedDestinations: string[];
}

export interface VpnClientResponse {
  downloadLink: string;
  client: VpnClient;
}

@Injectable({
  providedIn: 'root'
})
export class VpnClientService {

  private baseUrl = 'http://localhost:8080/api/v1/clients';
  private backendUrl = 'http://localhost:8080'; //

  constructor(private http: HttpClient) {}


  getAllClients(): Observable<VpnClient[]> {
    return this.http.get<VpnClient[]>(`${this.baseUrl}`, { withCredentials: true });
  }


  getClientByCn(cn: string): Observable<VpnClient> {
    return this.http.get<VpnClient>(`${this.baseUrl}/${cn}`, { withCredentials: true });
  }

  getIpRanges() {
    return this.http.get<any[]>(`${this.baseUrl}/ranges`, { withCredentials: true });
  }

  createClient(client: any): Observable<VpnClientResponse> {
    return this.http.post<VpnClientResponse>(`${this.baseUrl}`, client, { withCredentials: true });
  }

  downloadClientConfig(downloadLink: string) {
    return   this.http.get(this.backendUrl + downloadLink, {
      responseType: 'blob',
      observe: 'response',
      withCredentials: true
      });
  }

  getCertdHealth(): Observable<any> {
    return this.http.get<any>(`${this.baseUrl}/certd/health`, { withCredentials: true });
  }
}
