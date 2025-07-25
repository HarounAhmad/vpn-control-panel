import {CanActivate, CanActivateFn, Router} from '@angular/router';
import {Injectable} from "@angular/core";
import {AuthService} from "../service/auth.service";
import {catchError, map, Observable, of} from "rxjs";

@Injectable({ providedIn: 'root' })
export class AuthGuard implements CanActivate {

  constructor(private auth: AuthService, private router: Router) {}

  canActivate(): Observable<boolean> {
    return this.auth.loadUserInfo().pipe(
      map(() => true),
      catchError(() => {
        this.router.navigate(['']);
        return of(false);
      })
    );
  }
}
