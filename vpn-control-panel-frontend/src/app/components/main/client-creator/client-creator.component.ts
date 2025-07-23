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
  private octetValid: boolean;

  constructor(private clientService: VpnClientService) {
  }

  ranges: any[] = [];

  clientTypes: { label: string; value: string }[] = [];

  selectedClientType: any;
  lastOctet: any;

  selectedRange: { start: number; end: number } = { start: 0, end: 0 };

  ngOnInit() {
    this.clientService.getIpRanges().subscribe((data: any) => {
        this.ranges = [...data]
        this.clientTypes = this.ranges.map((range: any) => {
          console.log(range)
          return {
            label: range.clientType.charAt(0).toUpperCase() + range.clientType.slice(1).toLowerCase(),
            value: range.clientType
          };
        })
    });
  }

  createClient() {

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

  validateIp() {

    this.ipValid = this.ipRegex.test(this.newEntry.trim());
  }

  onSelectClientType() {
    this.selectedRange = this.ranges.find(range => range.clientType === this.selectedClientType);
    if (this.selectedRange) {
      this.selectedRange = { start: this.selectedRange.start, end: this.selectedRange.end };
      this.lastOctet = '';
      this.octetValid = false;
    }
  }


  validateOctet(): void {
    const num = parseInt(this.lastOctet, 10);
    this.octetValid = !isNaN(num) && num >= this.selectedRange.start && num <= this.selectedRange.start;
  }
}
