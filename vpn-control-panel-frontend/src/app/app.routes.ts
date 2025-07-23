import { Routes } from '@angular/router';
import {ClientSelectorComponent} from "./components/main/client-selector/client-selector.component";
import {ClientRulesComponent} from "./components/main/client-rules/client-rules.component";

export const routes: Routes = [
  {
    'path': '',
    component: ClientSelectorComponent
  },
  {
    path: 'client/:cn',
    component: ClientRulesComponent
  }


];
