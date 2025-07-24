import {Component, OnInit} from '@angular/core';
import {AuditLog, AuditLogService} from "../../../service/audit-log.service";
import {TitleBarComponent} from "../blocks/title-bar/title-bar.component";
import {TableModule} from "primeng/table";
import {NgForOf, NgIf, NgSwitch, NgSwitchCase, NgSwitchDefault} from "@angular/common";
import {FormatDatePipe} from "../../../pipes/format-date.pipe";
import {AuditDetailsFormatPipePipe} from "../../../pipes/audit-details-format-pipe.pipe";
import {TruncatePipe} from "../../../pipes/truncate.pipe";

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
    TruncatePipe
  ],
  templateUrl: './audit-log.component.html',
  standalone: true,
  styleUrl: './audit-log.component.scss'
})
export class AuditLogComponent implements OnInit{

  logs: AuditLog[] = [];
  cols: any[] = [];
  constructor(
    private auditLogService: AuditLogService
  ) {
  }

  ngOnInit() {
    this.getLogs();
    this.cols = [
      { field: 'id', header: 'ID' },
      { field: 'action', header: 'Action' },
      { field: 'entityType', header: 'Entity Type' },
      { field: 'entityId', header: 'Entity ID' },
      { field: 'details', header: 'Details' },
      { field: 'performedBy', header: 'Performed By' },
      { field: 'timestamp', header: 'Timestamp' },
      { field: 'oldValue', header: 'Old Value' },
      { field: 'newValue', header: 'New Value' }
    ];
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

}
