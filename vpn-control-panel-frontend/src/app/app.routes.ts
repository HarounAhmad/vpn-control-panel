import { Routes } from '@angular/router';
import {ClientSelectorComponent} from "./components/main/client-selector/client-selector.component";
import {ClientRulesComponent} from "./components/main/client-rules/client-rules.component";
import {ClientCreatorComponent} from "./components/main/client-creator/client-creator.component";
import {MainComponent} from "./components/main/main/main.component";
import {AuditLogComponent} from "./components/main/audit-log/audit-log.component";
import {LoginComponent} from "./components/auth/login/login.component";
import {AuthGuard} from "./auth/auth.guard";
import {LoginGuard} from "./auth/login.guard";
import {RoleGuard} from "./auth/role.guard";

export const routes: Routes = [
  {
    'path': '',
    component: LoginComponent
    ,canActivate: [LoginGuard]
  },
  {
    'path': 'clients',
    component: ClientSelectorComponent,
    canActivate: [AuthGuard, RoleGuard],
    data: { roles: ['SYSADMIN'] }
  },
  {
    'path': 'client/create',
    component: ClientCreatorComponent,
    canActivate: [AuthGuard, RoleGuard],
    data: { roles: ['SYSADMIN'] }
  },
  {
    'path': 'audit-logs',
    component: AuditLogComponent,
    canActivate: [AuthGuard, RoleGuard],
    data: { roles: ['SYSADMIN', 'AUDITOR']
    }
  }


];
