import {Component, OnInit} from '@angular/core';
import {AuditLog, AuditLogService} from "../../../service/audit-log.service";
import {TitleBarComponent} from "../blocks/title-bar/title-bar.component";
import {TableModule} from "primeng/table";
import {NgForOf, NgIf, NgSwitch, NgSwitchCase, NgSwitchDefault} from "@angular/common";
import {FormatDatePipe} from "../../../pipes/format-date.pipe";
import {AuditDetailsFormatPipePipe} from "../../../pipes/audit-details-format-pipe.pipe";
import {TruncatePipe} from "../../../pipes/truncate.pipe";
import {ButtonDirective} from "primeng/button";
import {Dialog} from "primeng/dialog";
import {Divider} from "primeng/divider";

@Component({
  selector: 'app-audit-log',
  imports: [
    TitleBarComponent,
    TableModule,
    NgForOf,
    FormatDatePipe,
    NgIf,
    AuditDetailsFormatPipePipe,
    NgSwitchCase,
    NgSwitch,
    NgSwitchDefault,
    TruncatePipe,
    ButtonDirective,
    Dialog,
    Divider
  ],
  templateUrl: './audit-log.component.html',
  standalone: true,
  styleUrl: './audit-log.component.scss'
})
export class AuditLogComponent implements OnInit{

  logs: AuditLog[] = [];
  cols: any[] = [];

  selectedLog: AuditLog | null = null;
  detailsVisible: boolean = false;

  constructor(
    private auditLogService: AuditLogService
  ) {
  }

  ngOnInit() {
    this.getLogs();
    this.cols = [
      { field: 'action', header: 'Action' },
      { field: 'entityType', header: 'Entity Type' },
      { field: 'summary', header: 'Summary' },
      { field: 'performedBy', header: 'Performed By' },
      { field: 'timestamp', header: 'Timestamp' },
      { field: 'details', header: 'Details'}
    ];
  }

  get entries(): [string, any][] {
    return this.selectedLog?.details
      ? Object.entries(this.selectedLog.details)
      : [];
  }
  getLogs() {
    this.auditLogService.getAuditLogs().subscribe({
      next: (logs) => {
        this.logs = [];
        this.logs = [...logs]
        console.log('Audit logs fetched successfully:', this.logs);
      },
      error: (error) => {
        console.error('Error fetching audit logs:', error);
      }
    });
  }

  showDetails(rowData: any) {
    this.selectedLog = rowData;
    this.detailsVisible = true;
    console.log('Selected log details:', this.selectedLog);
  }

  hideDetails() {
    this.selectedLog = null;
    this.detailsVisible = false;
  }

  formatKey(detailElement: string) {
  if (detailElement) {
    const parts = detailElement.split('.');
    if (parts.length > 1) {
      return parts.map(part => part.charAt(0).toUpperCase() + part.slice(1)).join(' ');
    }
    return detailElement.charAt(0).toUpperCase() + detailElement.slice(1);
  }
    return "";
  }
}
