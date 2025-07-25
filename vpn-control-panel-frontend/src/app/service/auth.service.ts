import {Injectable, OnInit} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {map, Observable, switchMap, tap} from "rxjs";


interface UserInfo {
  username: string;
  roles: string[];
}

@Injectable({
  providedIn: 'root'
})
export class AuthService implements OnInit{

  baseUrl = 'http://localhost:8080/api/v1/auth';
  private userInfo: UserInfo | null = null;

  constructor(
    private http: HttpClient
  ) { }

  ngOnInit() {

  }

  login(username: string, password: string): Observable<void> {
    return this.http.post<void>(`${this.baseUrl}/login`, { username, password }, { withCredentials: true }).pipe(
      switchMap(() => this.loadUserInfo()),
      map(() => void 0)
    );
  }

  loadUserInfo(): Observable<UserInfo> {
    return this.http.get<UserInfo>(`${this.baseUrl}/me`, { withCredentials: true }).pipe(
      tap(user => this.userInfo = user)
    );
  }

  logout(): Observable<void> {
    return this.http.post<void>(`${this.baseUrl}/logout`, {}, { withCredentials: true }).pipe(
      tap(() => this.userInfo = null)
    );
  }

  isLoggedIn(): boolean {
    return this.userInfo !== null;
  }

  getUsername(): string {
    return this.userInfo?.username || '';
  }

  hasRole(role: string): boolean {
    return this.userInfo?.roles.includes(role) || false;
  }

  getUser(): UserInfo | null {
    return this.userInfo;
  }
}
