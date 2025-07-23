import {Component, OnInit} from '@angular/core';
import {FormsModule} from "@angular/forms";
import {NgForOf, NgIf} from "@angular/common";
import {ClientRulesComponent} from "../client-rules/client-rules.component";
import {VpnClient, VpnClientService} from "../../../service/vpn-client.service";
import {TableModule} from "primeng/table";
import {Router} from "@angular/router";
import {TitleBarComponent} from "../blocks/title-bar/title-bar.component";

@Component({
  selector: 'app-client-selector',
  imports: [
    FormsModule,
    NgForOf,
    ClientRulesComponent,
    TableModule,
    TitleBarComponent,
    NgIf
  ],
  templateUrl: './client-selector.component.html',
  standalone: true,
  styleUrl: './client-selector.component.scss'
})
export class ClientSelectorComponent implements OnInit{
  clients: VpnClient[] = [];
  // @ts-ignore
  selectedClient: VpnClient;
  columns: any[] = [];


  constructor(private vpnClientService: VpnClientService, private router: Router) {
  }

  ngOnInit(): void {
    this.columns = [
      { field: 'cn', header: 'Common Name' },
    ]


    this.vpnClientService.getAllClients().subscribe(c => {
      this.clients = [...c];
      console.log('Clients loaded:', this.clients)
      if (this.clients.length > 0) {
      }
    })

  }

  onRowSelect() {
    console.log('Selected client:', this.selectedClient)
    this.router.navigate(['/client', this.selectedClient.cn]);

  }
}
