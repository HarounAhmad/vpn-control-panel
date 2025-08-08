import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {map, Observable, switchMap} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class ClientStatusService {

  baseUrl = 'http://localhost:8080/api/v1/client-status';

  constructor(
    private http: HttpClient
  ) { }


  getByCn(cn: string): Observable<any> {
    return this.http.get<any>(`${this.baseUrl}/get/${cn}`, { withCredentials: true });
  }

  getAll(): Observable<any[]> {
    return this.http.get<any[]>(`${this.baseUrl}/get`, { withCredentials: true });
  }
}
