import {Component, Input, OnInit} from '@angular/core';
import {ClientStatus} from "../../../model/client-status";
import {Card} from "primeng/card";
import {Toolbar} from "primeng/toolbar";
import {FormsModule} from "@angular/forms";
import {TableModule} from "primeng/table";
import {Badge} from "primeng/badge";
import {ButtonDirective} from "primeng/button";
import {BytesPipe} from "../../../pipes/bytes.pipe";
import {DatePipe, NgIf} from "@angular/common";
import {ClientStatusService} from "../../../service/client-status.service";

@Component({
  selector: 'app-client-status',
  imports: [
    Card,
    Toolbar,
    FormsModule,
    TableModule,
    Badge,
    ButtonDirective,
    BytesPipe,
    DatePipe,
    NgIf
  ],
  templateUrl: './client-status.component.html',
  standalone: true,
  styleUrl: './client-status.component.scss'
})
export class ClientStatusComponent implements OnInit{

  @Input()
  set clientCn(value: string) {
    this._clientCnValue = value;
    this.reload()
  }

  private _clientCnValue!: string;
  clientStatus: ClientStatus = new ClientStatus();

  constructor(
    private clientStatusService: ClientStatusService
  ) {
  }

  ngOnInit() {
      this.reload();

  }


  get sessionAge(): string {
    if (!this.clientStatus?.connectedSince) return '-';
    const d = new Date(this.clientStatus.connectedSince);
    const ms = Date.now() - d.getTime();
    const s = Math.floor(ms / 1000);
    const h = Math.floor(s / 3600);
    const m = Math.floor((s % 3600) / 60);
    const ss = s % 60;
    return h > 0 ? `${h}h ${m}m` : `${m}m ${ss}s`;
  }


  reload() {
    if (this._clientCnValue) {
      this.clientStatusService.getByCn(this._clientCnValue).subscribe(status => {
        this.clientStatus = status;
      });
    }
  }



}
