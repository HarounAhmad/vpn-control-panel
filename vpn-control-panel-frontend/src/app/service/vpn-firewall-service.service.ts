import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";


export interface NftRule {
  clientCn: string;
  srcIp: string;
  dstIp: string;
  protocol: string;
  dstPort: number;
  response: string;
}


@Injectable({
  providedIn: 'root'
})
export class VpnFirewallService {
  private baseUrl = 'http://localhost:8080/api/v1/clients';

  constructor(private http: HttpClient) {}

  listRules(clientCn: string): Observable<NftRule[]> {
    return this.http.get<NftRule[]>(`${this.baseUrl}/${clientCn}/rules`, { withCredentials: true });
  }

  addRule(clientCn: string, rule: NftRule): Observable<NftRule> {
    console.log(rule)
    return this.http.post<NftRule>(`${this.baseUrl}/${clientCn}/rules`, rule, { withCredentials: true });
  }

  removeRule(clientCn: string, rule: NftRule): Observable<void> {
    return this.http.request<void>('delete', `${this.baseUrl}/${clientCn}/rules`, { body: rule, withCredentials: true });
  }

  replaceRules(clientCn: string, rules: NftRule[]): Observable<void> {
    return this.http.put<void>(`${this.baseUrl}/${clientCn}/rules`, rules, { withCredentials: true });
  }



}
