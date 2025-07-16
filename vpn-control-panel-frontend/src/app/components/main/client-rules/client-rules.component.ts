import {Component, Input, OnInit} from '@angular/core';
import {NftRule, VpnFirewallService} from "../../../service/vpn-firewall-service.service";
import {FormsModule} from "@angular/forms";
import {NgForOf} from "@angular/common";

@Component({
  selector: 'app-client-rules',
  standalone: true,
  imports: [
    FormsModule,
    NgForOf
  ],
  templateUrl: './client-rules.component.html',
  styleUrl: './client-rules.component.scss'
})
export class ClientRulesComponent implements OnInit {
  @Input() clientCn!: string;
  rules: NftRule[] = [];
  newPort = 0;
  newProtocol: 'tcp' | 'udp' = 'tcp';

  constructor(private vpnService: VpnFirewallService) {}

  ngOnInit(): void {
    this.loadRules();
  }

  loadRules() {
    this.vpnService.listRules(this.clientCn).subscribe(r => (this.rules = r));
  }

  addRule() {
    const srcIp = this.rules.length ? this.rules[0].srcIp : '10.8.0.x'; // replace accordingly
    const dstIp = this.rules.length ? this.rules[0].dstIp : '10.8.0.1';
    if (!this.newPort) return;

    const rule: NftRule = {
      clientCn: this.clientCn,
      srcIp,
      dstIp,
      protocol: this.newProtocol,
      dstPort: this.newPort,
    };

    this.vpnService.addRule(this.clientCn, rule).subscribe(() => {
      this.newPort = 0;
      this.loadRules();
    });
  }

  removeRule(rule: NftRule) {
    this.vpnService.removeRule(this.clientCn, rule).subscribe(() => this.loadRules());
  }
}
