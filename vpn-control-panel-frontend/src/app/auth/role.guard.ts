import {Injectable} from "@angular/core";
import {ActivatedRouteSnapshot, CanActivate, Router} from "@angular/router";
import {AuthService} from "../service/auth.service";
import {catchError, map, Observable, of} from "rxjs";

@Injectable({ providedIn: 'root' })
export class RoleGuard implements CanActivate {
  constructor(private auth: AuthService, private router: Router) {}

  canActivate(route: ActivatedRouteSnapshot): Observable<boolean> {
    const expectedRoles = route.data['roles'] as string[] | undefined;
    return this.auth.loadUserInfo().pipe(
      map(user => {
        if (!expectedRoles) return true;
        const hasMatch = expectedRoles.some(role => user.roles.includes(role));
        if (!hasMatch && this.auth.hasRole("AUDITOR")) this.router.navigate(['/audit-logs']);
        else if (!hasMatch) this.router.navigate(['']);
        return hasMatch;
      }),
      catchError(() => {
        this.router.navigate(['']);
        return of(false);
      })
    );
  }
}
