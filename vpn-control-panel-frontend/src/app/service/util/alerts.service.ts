import { Injectable } from '@angular/core';
import {Alert} from "../../model/alert";
import {BehaviorSubject} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class AlertsService {

  constructor(
  ) { }

  public alertEmitter: BehaviorSubject<Alert> = new BehaviorSubject<Alert>(new Alert());

  private defaultLife = 5000;

  public success(summary: string, detail: string) {
    const alert: Alert = {
      severity: 'success',
      summary: summary,
      detail: detail,
      life: undefined,
      sticky: true
    };

    this.alertEmitter.next(alert);
  }

  public successSelfClosing(summary: string, detail: string) {
    const alert: Alert = {
      severity: 'success',
      summary: summary,
      detail: detail,
      life: this.defaultLife,
      sticky: false
    };
    console.log('Alert created:', alert);

    this.alertEmitter.next(alert);

  }

  public warning(summary: string, detail: string) {
    const alert: Alert = {
      severity: 'warn',
      summary: summary,
      detail: detail,
      life: undefined,
      sticky: true
    };

    this.alertEmitter.next(alert);

  }

  public warningSelfClosing(summary: string, detail: string) {
    const alert: Alert = {
      severity: 'warn',
      summary: summary,
      detail: detail,
      life: this.defaultLife,
      sticky: false
    };

    this.alertEmitter.next(alert);
  }

  public error(summary: string, detail: string) {
    const alert: Alert = {
      severity: 'error',
      summary: summary,
      detail: detail,
      life: undefined,
      sticky: true
    };

    this.alertEmitter.next(alert);

  }

  public errorSelfClosing(summary: string, detail: string) {
    const alert: Alert = {
      severity: 'error',
      summary: summary,
      detail: detail,
      life: this.defaultLife,
      sticky: false
    };

    this.alertEmitter.next(alert);
  }

  public info(summary: string, detail: string) {
    const alert: Alert = {
      severity: 'info',
      summary: summary,
      detail: detail,
      life: undefined,
      sticky: true
    };

    this.alertEmitter.next(alert);
  }

  public infoSelfClosing(summary: string, detail: string) {
    const alert: Alert = {
      severity: 'info',
      summary: summary,
      detail: detail,
      life: this.defaultLife,
      sticky: false
    };

    this.alertEmitter.next(alert);
  }

  public clear() {
    this.alertEmitter.next(new Alert());
  }

}
