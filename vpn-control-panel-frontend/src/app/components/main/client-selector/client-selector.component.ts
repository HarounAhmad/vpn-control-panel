import {Component, OnInit} from '@angular/core';
import {FormsModule} from "@angular/forms";
import {NgForOf} from "@angular/common";
import {ClientRulesComponent} from "../client-rules/client-rules.component";
import {VpnClient, VpnClientService} from "../../../service/vpn-client.service";

@Component({
  selector: 'app-client-selector',
  standalone: true,
  imports: [
    FormsModule,
    NgForOf,
    ClientRulesComponent
  ],
  templateUrl: './client-selector.component.html',
  styleUrl: './client-selector.component.scss'
})
export class ClientSelectorComponent implements OnInit{
  clients: VpnClient[] = [];
  selectedClient = this.clients[0];


  constructor(private vpnClientService: VpnClientService) {
  }

  ngOnInit(): void {
    this.vpnClientService.getAllClients().subscribe(c => {
      this.clients = [...c];
      console.log('Clients loaded:', this.clients)
      if (this.clients.length > 0) {
        this.selectedClient = this.clients[0];
      }
    })

  }
}
