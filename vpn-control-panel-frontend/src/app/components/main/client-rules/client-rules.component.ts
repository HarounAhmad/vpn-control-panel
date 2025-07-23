import {Component, Input, OnInit} from '@angular/core';
import {NftRule, VpnFirewallService} from "../../../service/vpn-firewall-service.service";
import {FormsModule} from "@angular/forms";
import {NgForOf, NgIf} from "@angular/common";
import {ActivatedRoute} from "@angular/router";
import {TableModule} from "primeng/table";
import {InputText} from "primeng/inputtext";
import {DropdownModule} from "primeng/dropdown";
import {Select} from "primeng/select";
import {Button, ButtonDirective} from "primeng/button";
import {VpnClientService} from "../../../service/vpn-client.service";

@Component({
  selector: 'app-client-rules',
  imports: [
    FormsModule,
    NgForOf,
    TableModule,
    NgIf,
    InputText,
    DropdownModule,
    Select,
    ButtonDirective,
    Button
  ],
  templateUrl: './client-rules.component.html',
  standalone: true,
  styleUrl: './client-rules.component.scss'
})
export class ClientRulesComponent implements OnInit {
  clientCn!: string;
  rules: NftRule[] = [];
  newPort = 0;
  protocolOptions = [
    { name: 'TCP', value: 'tcp' },
    { name: 'UDP', value: 'udp' }
  ];

  client: any;

  newProtocol: any;
  cols: any[] = []

  constructor(private vpnService: VpnFirewallService, private route: ActivatedRoute, private vpnClientService: VpnClientService) {}

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      const cn = params.get('cn');
      if (cn) {
        this.clientCn = cn;
      }
    });
    this.loadCLient()
    this.cols = [
      { field: 'srcIp', header: 'Source IP' },
      { field: 'dstIp', header: 'Destination IP' },
      { field: 'protocol', header: 'Protocol' },
      { field: 'dstPort', header: 'Destination Port' },
      { field: 'actions', header: 'Actions' }
    ];
  }

  loadRules() {
    this.vpnService.listRules(this.clientCn).subscribe(r => (this.rules = r));
  }
  loadCLient() {
    this.vpnClientService.getClientByCn(this.clientCn).subscribe(client => {
      if (client) {
        this.client = client;
        this.loadRules();
      }
    });
  }

  addRule() {
    const srcIp = this.client.ccd.ip
    const dstIp = this.client.allowedDestinations[0];
    if (!this.newPort) return;

    const rule: NftRule = {
      clientCn: this.clientCn,
      srcIp,
      dstIp,
      protocol: this.newProtocol?.value,
      dstPort: this.newPort,
    };

    this.vpnService.addRule(this.clientCn, rule).subscribe(() => {
      this.newPort = 0;
      this.loadRules();
    });
  }

  deleteRule(rule: NftRule) {
    this.vpnService.removeRule(this.clientCn, rule).subscribe(() => this.loadRules());

  }
}
