import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  standalone: true,
  name: 'truncate'
})
export class TruncatePipe implements PipeTransform {

  transform(value: string, maxLength: number = 100): string {
    if (!value) return '';
    return value.length <= maxLength ? value : value.slice(0, maxLength) + '...';
  }

}
