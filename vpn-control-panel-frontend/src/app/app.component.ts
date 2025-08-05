import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import {ClientRulesComponent} from "./components/main/client-rules/client-rules.component";
import {ClientSelectorComponent} from "./components/main/client-selector/client-selector.component";
import {AlertsComponent} from "./components/util/alerts/alerts.component";

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, ClientRulesComponent, ClientSelectorComponent, AlertsComponent],
  templateUrl: './app.component.html',
  standalone: true,
  styleUrl: './app.component.scss'
})
export class AppComponent {
  title = 'vpn-control-panel-frontend';
}
