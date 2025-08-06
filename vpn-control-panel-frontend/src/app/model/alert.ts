export class Alert {
  severity: 'success' | 'error' | 'info' | 'warn' = 'success';
  summary: string = '';
  detail: string = '';
  life?: number = undefined;
  sticky: boolean = false;
}
