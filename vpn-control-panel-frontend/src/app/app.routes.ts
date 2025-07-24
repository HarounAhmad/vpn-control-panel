import { Routes } from '@angular/router';
import {ClientSelectorComponent} from "./components/main/client-selector/client-selector.component";
import {ClientRulesComponent} from "./components/main/client-rules/client-rules.component";
import {ClientCreatorComponent} from "./components/main/client-creator/client-creator.component";
import {MainComponent} from "./components/main/main/main.component";

export const routes: Routes = [
  {
    'path': '',
    component: MainComponent
  },
  {
    'path': 'clients',
    component: ClientSelectorComponent
  },
  {
    'path': 'client/create',
    component: ClientCreatorComponent
  }


];
