import {Component, Input} from '@angular/core';
import {VpnClientService} from "../../../service/vpn-client.service";
import {AlertsService} from "../../../service/util/alerts.service";
import {NgIf} from "@angular/common";
import {Button} from "primeng/button";

@Component({
  selector: 'app-client-files',
  imports: [
    NgIf,
    Button
  ],
  templateUrl: './client-files.component.html',
  standalone: true,
  styleUrl: './client-files.component.scss'
})
export class ClientFilesComponent {

  @Input()
  set clientCn(value: string) {
    this._clientCnValue = value;
    this.loadClient()
  }

  private _clientCnValue!: string;

  clientFiles: string[] = [];

 constructor(
   private vpnClientService: VpnClientService,
   private alertService: AlertsService
 ) {
 }

  loadClient() {
    // This method should call the service to get the client files based on the clientCn
    // For now, we will just simulate loading files

  }


  downloadConfig() {
   this.vpnClientService.downloadClientConfig("/api/v1/clients/" + this._clientCnValue + "/config/download").subscribe({
     next: (response) => {
       const blob = new Blob([response.body!], { type: 'application/octet-stream' });
       const filename = this._clientCnValue + ".ovpn" || `client.ovpn`;

       const link = document.createElement('a');
       link.href = URL.createObjectURL(blob);
       link.download = filename;
       link.click();

       URL.revokeObjectURL(link.href);
       this.alertService.successSelfClosing('Client config retrieved successfully', filename);
     },
     error: (error) => {
       this.alertService.error('Error downloading client config', error.message);

     }
   });
  }



}
