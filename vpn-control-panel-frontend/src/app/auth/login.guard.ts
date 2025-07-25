import {Injectable} from "@angular/core";
import {CanActivate, Router} from "@angular/router";
import {AuthService} from "../service/auth.service";
import {catchError, map, Observable, of} from "rxjs";

@Injectable({ providedIn: 'root' })
export class LoginGuard implements CanActivate {
  constructor(private auth: AuthService, private router: Router) {}

  canActivate(): Observable<boolean> {
    return this.auth.loadUserInfo().pipe(
      map(() => {
        this.router.navigate(['/clients']);
        return false;
      }),
      catchError(() => of(true))
    );
  }
}
