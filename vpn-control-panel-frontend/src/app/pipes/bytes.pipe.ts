import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  standalone: true,
  name: 'bytes'
})
export class BytesPipe implements PipeTransform {

  transform(v?: number): string {
    if (v == null) return '0 B';
    const u = ['B','KB','MB','GB','TB'];
    let i = 0, n = v;
    while (n >= 1024 && i < u.length - 1) { n /= 1024; i++; }
    return `${n < 10 && i ? n.toFixed(1) : Math.round(n)} ${u[i]}`;
  }
}
