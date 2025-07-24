import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  standalone: true,
  name: 'auditDetailsFormatPipe'
})
export class AuditDetailsFormatPipePipe implements PipeTransform {

  transform(value: string): string {
    if (!value) return '';

    if (this.isOpenVpnConfig(value)) {
      return this.formatOpenVpnConfig(value);
    }

    if (this.isJavaToString(value)) {
      return this.formatJavaToString(value);
    }

    return value;
  }

  private isOpenVpnConfig(value: string): boolean {
    return value.startsWith('client') &&
      value.includes('dev') &&
      value.includes('proto') &&
      value.includes('remote');
  }

  private isJavaToString(value: string): boolean {
    return /^[A-Z][a-zA-Z0-9]*\(.+\)$/.test(value.trim());
  }

  private formatOpenVpnConfig(value: string): string {
    return value.replace(/\s+/g, '\n');
  }

  private formatJavaToString(value: string): string {
    const [className, bodyRaw] = value.split('(', 2);
    const body = bodyRaw?.replace(/\)$/, '') ?? '';
    return `${className}\n` + body
      .split(', ')
      .map(entry => entry.replace('=', ': '))
      .join('\n');
  }

}
