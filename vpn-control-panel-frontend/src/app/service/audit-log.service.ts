import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";



export interface AuditLog {
  id: string,
  action: string,
  entityType: string,
  entityId: string,
  details: string,
  performedBy: string,
  timestamp: string
  oldValue?: string,
  newValue?: string
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
      return this.http.get<AuditLog[]> (`${this.baseUrl}`);
  }
}
