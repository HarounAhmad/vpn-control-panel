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
import {IconField} from "primeng/iconfield";
import {InputIcon} from "primeng/inputicon";
import {Toolbar} from "primeng/toolbar";
import {TitleBarComponent} from "../blocks/title-bar/title-bar.component";
import {Card} from "primeng/card";
import {InputNumber} from "primeng/inputnumber";
import {InputGroupAddon} from "primeng/inputgroupaddon";
import {InputGroup} from "primeng/inputgroup";
import {SelectButton} from "primeng/selectbutton";
import {AlertsService} from "../../../service/util/alerts.service";

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
    Button,
    IconField,
    InputIcon,
    Toolbar,
    TitleBarComponent,
    Card,
    InputNumber,
    InputGroupAddon,
    InputGroup,
    SelectButton
  ],
  templateUrl: './client-rules.component.html',
  standalone: true,
  styleUrl: './client-rules.component.scss'
})
export class ClientRulesComponent implements OnInit {
  @Input()
  set clientCn(value: string) {
    this._clientCnValue = value;
    this.loadClient()
  }

  private _clientCnValue!: string;

  rules: NftRule[] = [];
  newPort = 0;
  protocolOptions = [
    { label: 'TCP', value: 'tcp' },
    { label: 'UDP', value: 'udp' }
  ];

  client: any;

  newProtocol: any;
  cols: any[] = []

  constructor(private vpnService: VpnFirewallService, private route: ActivatedRoute, private vpnClientService: VpnClientService, private alertService: AlertsService) {}

  ngOnInit(): void {
    this.newProtocol = this.protocolOptions[0].value;
    this.cols = [
      { field: 'srcIp', header: 'Source IP' },
      { field: 'dstIp', header: 'Destination IP' },
      { field: 'protocol', header: 'Protocol' },
      { field: 'dstPort', header: 'Destination Port' },
      { field: 'actions', header: 'Actions' }
    ];
  }

  loadRules() {
    this.vpnService.listRules(this._clientCnValue).subscribe(r => (this.rules = r));
  }

  loadClient() {
    console.log('Loading client with CN:', this._clientCnValue)
    this.vpnClientService.getClientByCn(this._clientCnValue).subscribe(client => {
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
      clientCn: this._clientCnValue,
      srcIp,
      dstIp,
      protocol: this.newProtocol,
      dstPort: this.newPort,
      response: ""
    };

    this.vpnService.addRule(this._clientCnValue, rule).subscribe(data => {
      this.newPort = 0;
      this.rules.push(data)
      this.alertService.successSelfClosing("Success", "Rule added Successfully")
      if (data.response.startsWith("WARNING")) {
        this.alertService.warning("Warning", data.response)
      } else if (data.response.startsWith("SUCCESS")) {
        this.alertService.warning("Success", data.response)
      }
    });
  }

  deleteRule(rule: NftRule) {
    this.vpnService.removeRule(this._clientCnValue, rule).subscribe(() => this.loadRules());

  }
}
