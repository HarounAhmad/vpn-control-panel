import {Component, NgZone, OnInit} from '@angular/core';
import {AlertsService} from "../../../service/util/alerts.service";
import {MessageService} from "primeng/api";
import {Alert} from "../../../model/alert";
import {Toast, ToastModule} from "primeng/toast";

@Component({
  selector: 'app-alerts',
  standalone: true,
  imports: [ToastModule],
  templateUrl: './alerts.component.html',
  styleUrls: ['./alerts.component.scss'],
  providers: [{ provide: MessageService, useClass: MessageService }]
})

export class AlertsComponent implements OnInit {


  constructor(
    private alertService: AlertsService,
    private messageService: MessageService,
    private zone: NgZone
  ) {
  }


  ngOnInit() {
    this.alertService.alertEmitter.subscribe((alert: Alert) => {
      if (alert.summary === '') {
        this.messageService.clear();
        return;
      }
      this.throwAlert(alert);
    });
  }

  throwAlert(alert: Alert) {
    this.zone.run(() => this.messageService.add(alert));
  }

}
