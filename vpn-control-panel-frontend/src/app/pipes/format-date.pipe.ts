import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  standalone: true,
  name: 'formatDate'
})
export class FormatDatePipe implements PipeTransform {

  transform(value: string): string {
    if (!value) return '';
    const date = new Date(value);
    return date.toLocaleString(); // optional: provide locale and options
  }

}
