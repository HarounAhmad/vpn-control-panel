import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";



export interface AuditLog {
  action: string;
  entityType: string;
  summary: string;
  timestamp: string;
  performedBy: string;
  details: Map<string, string>
}
@Injectable({
  providedIn: 'root'
})
export class AuditLogService {

  baseUrl = 'http://localhost:8080/api/v1/audit-logs';
  constructor(
    private http: HttpClient
  ) { }

  getAuditLogs(): Observable<AuditLog[]> {
      return this.http.get<AuditLog[]> (`${this.baseUrl}`, {withCredentials: true});
  }
}
