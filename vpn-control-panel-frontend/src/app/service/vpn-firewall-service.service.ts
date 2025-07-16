import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";


export interface NftRule {
  clientCn: string;
  srcIp: string;
  dstIp: string;
  protocol: string;
  dstPort: number;
}


@Injectable({
  providedIn: 'root'
})
export class VpnFirewallService {
  private baseUrl = 'http://localhost:8080/api/v1/clients';

  constructor(private http: HttpClient) {}

  listRules(clientCn: string): Observable<NftRule[]> {
    return this.http.get<NftRule[]>(`${this.baseUrl}/${clientCn}/rules`);
  }

  addRule(clientCn: string, rule: NftRule): Observable<void> {
    return this.http.post<void>(`${this.baseUrl}/${clientCn}/rules`, rule);
  }

  removeRule(clientCn: string, rule: NftRule): Observable<void> {
    return this.http.request<void>('delete', `${this.baseUrl}/${clientCn}/rules`, { body: rule });
  }

  replaceRules(clientCn: string, rules: NftRule[]): Observable<void> {
    return this.http.put<void>(`${this.baseUrl}/${clientCn}/rules`, rules);
  }



}
