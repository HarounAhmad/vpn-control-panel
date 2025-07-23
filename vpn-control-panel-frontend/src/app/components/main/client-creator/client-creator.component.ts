import {Component, OnInit} from '@angular/core';
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


export interface Client {
  cn: string;
  assignedIp: string;
  allowedDestinations: string[];
  description?: string;
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
export class ClientCreatorComponent implements OnInit{
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
  lastOctet: any;

  selectedRange: { start: number; end: number } = { start: 0, end: 0 };

  ngOnInit() {
    this.clientService.getIpRanges().subscribe((data: any) => {
        this.ranges = [...data]
        this.clientTypes = this.ranges.map((range: any) => {
          this.ipPrefix = range.start.split('.').slice(0, 3).join('.') + '.';
          console.log(this.ipPrefix)
          return {
            label: range.clientType.charAt(0).toUpperCase() + range.clientType.slice(1).toLowerCase(),
            value: range.clientType
          };
        })
    });
  }

  createClient() {
      const client: Client = {
        cn: this.selectedClientType.toLowerCase() + "-" + this.clientName,
        assignedIp: this.ipPrefix + this.lastOctet,
        allowedDestinations: this.allowedDestinations,
        description: this.clientDescription
      }
      console.log(client);
      this.clientService.createClient(client).subscribe({
        next: (response) => {
          this.clientName = '';
          this.clientDescription = '';
          this.allowedDestinations = [];
          this.newEntry = '';
          this.lastOctet = '';
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
      this.lastOctet = '';
      this.octetValid = false;
    }
  }

  getLastOctet(ip: string): number {
    return parseInt(ip.split('.').pop() || '', 10);
  }


  validateOctet(): void {
    const num = parseInt(this.lastOctet, 10);
    this.octetValid = !isNaN(num) && num >= this.selectedRange.start && num <= this.selectedRange.start;
  }
}


