export class Alert {
  severity: 'success' | 'error' | 'info' | 'warning' = 'success';
  summary: string = '';
  detail: string = '';
  life?: number = undefined;
}
