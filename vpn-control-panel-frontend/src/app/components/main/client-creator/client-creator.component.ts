import {AfterViewInit, Component, OnInit} from '@angular/core';
import {TitleBarComponent} from "../blocks/title-bar/title-bar.component";
import {Card} from "primeng/card";
import {InputText} from "primeng/inputtext";
import {FormsModule} from "@angular/forms";
import {Button} from "primeng/button";
import {NgForOf, NgIf} from "@angular/common";
import {InputGroup} from "primeng/inputgroup";
import {InputGroupAddon} from "primeng/inputgroupaddon";
import {InputMask} from "primeng/inputmask";
import {VpnClientService} from "../../../service/vpn-client.service";
import {SelectButton} from "primeng/selectbutton";
import {InputNumber} from "primeng/inputnumber";
import { HttpResponse } from '@angular/common/http';


export interface Client {
  cn: string;
  assignedIp: string;
  allowedDestinations: string[];
  description?: string;
  clientType?: string;
}


@Component({
  selector: 'app-client-creator',
  imports: [
    TitleBarComponent,
    Card,
    InputText,
    FormsModule,
    Button,
    NgForOf,
    InputGroup,
    InputGroupAddon,
    InputMask,
    NgIf,
    SelectButton,
    InputNumber
  ],
  templateUrl: './client-creator.component.html',
  standalone: true,
  styleUrl: './client-creator.component.scss'
})
export class ClientCreatorComponent implements OnInit, AfterViewInit{
  clientName: any;
  clientDescription: any;
  clientIpAddress: any;

  allowedDestinations: string[] = [];
  newEntry: string = '';
  ipValid: boolean = true;
  private ipRegex =
    /^(25[0-5]|2[0-4]\d|1\d{2}|[1-9]?\d)(\.(25[0-5]|2[0-4]\d|1\d{2}|[1-9]?\d)){3}$/;
  private octetValid: boolean = true;

  constructor(private clientService: VpnClientService) {
  }

  ipPrefix: string = '10.8.0.';

  ranges: any[] = [];

  clientTypes: { label: string; value: string }[] = [];

  selectedClientType: any;
  lastOctet: number = 10;

  selectedRange: { start: number; end: number } = { start: 10, end: 255 };

  downloadLink: string = "";

  lastCn: string = '';

  ngOnInit() {
    this.clientService.getIpRanges().subscribe((data: any) => {
        this.ranges = [...data]
        this.clientTypes = this.ranges.map((range: any) => ({
          label: range.clientType.charAt(0).toUpperCase() + range.clientType.slice(1).toLowerCase(),
          value: range.clientType
        }));

      const selected = this.ranges.find(range => range.clientType === 'GUEST') || this.ranges[0];
      this.selectedClientType = selected.clientType;
      this.lastOctet = this.getLastOctet(selected.start);
      this.selectedRange = { start: this.getLastOctet(selected.start), end: this.getLastOctet(selected.end) };
      console.log(typeof this.selectedRange.start, typeof this.selectedRange.start,typeof this.selectedRange.end, typeof selected.start,typeof selected.end)
      this.ipPrefix = selected.start.split('.').slice(0, 3).join('.') + '.';

    });
  }

  ngAfterViewInit() {
    setTimeout(() => {
      this.lastOctet++; // trigger internal state update
      this.lastOctet--;
    });
  }

  createClient() {
      const client: Client = {
        cn: this.selectedClientType.toLowerCase() + "-" + this.clientName,
        assignedIp: this.ipPrefix + this.lastOctet,
        allowedDestinations: this.allowedDestinations,
        description: this.clientDescription,
        clientType: this.selectedClientType
      }
      this.clientService.createClient(client).subscribe({
        next: (response) => {
          this.lastCn = client.cn;
          this.reset();
          this.downloadLink = response.downloadLink;
          console.log(response)
        },
        error: (error) => {
          console.error('Error creating client:', error);
        }
      });
  }

  addEntry() {
    if (this.newEntry.trim()) {
      this.allowedDestinations.push(this.newEntry.trim());
      this.newEntry = '';
    }
  }

  removeEntry(i: number) {
    this.allowedDestinations.splice(i, 1);

  }

  onSelectClientType() {
    const range = this.ranges.find(range => range.clientType === this.selectedClientType);
    this.selectedRange = range ? { start: this.getLastOctet(range.start), end: this.getLastOctet(range.end)} : { start: 0, end: 0 };
    if (this.selectedRange) {
      this.selectedRange = { start: this.selectedRange.start, end: this.selectedRange.end };
      this.lastOctet = this.selectedRange.start;
      this.octetValid = false;
    }
  }

  getLastOctet(ip: string): number {
    return parseInt(ip.split('.').pop() || '', 10);
  }


  validateOctet(): void {
    const num = this.lastOctet;
    this.octetValid = !isNaN(num) && num >= this.selectedRange.start && num <= this.selectedRange.end;
  }

  debug(event: any) {
    console.log('value:', event.value, 'type:', typeof event.value);
    console.log('min :', this.selectedRange.start, 'type:', typeof this.selectedRange.start);
    console.log('max  :', this.selectedRange.end, 'type:', typeof this.selectedRange.end);
  }

  reset() {
    this.clientName = '';
    this.clientDescription = '';
    this.allowedDestinations = [];
    this.newEntry = '';
    this.lastOctet = this.getLastOctet(
      this.selectedRange.start.toString().split('.').slice(0, 3).join('.') + '.'
      + this.selectedRange.start)
  }

  cancel() {
    this.reset();
  }

  downloadClientConfig() {
    this.clientService.downloadClientConfig(this.downloadLink).subscribe({
      next: (response) => {
        const blob = new Blob([response.body!], { type: 'application/octet-stream' });
        const filename = this.lastCn + ".ovpn" || `client.ovpn`;

        const link = document.createElement('a');
        link.href = URL.createObjectURL(blob);
        link.download = filename;
        link.click();

        URL.revokeObjectURL(link.href);
      },
      error: (error) => {
        console.error('Error downloading client config:', error);
      }
    });
    }
}


