import {Injectable} from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class ColorService {

  constructor() {
  }

  setColor(data: object, name: string): void {
    const icon = document.getElementById('icon-' + name);
    if (icon != null) {
      if (data != null) {
        if (data) {
          icon.style.color = '#ff8b00';
        } else {
          icon.style.color = '#111111';
        }
      } else {
        icon.style.color = '#797979';
      }
    } else {
      console.error('Error occurred while setting color: ' + data + ' for: ' + name + ' | err: ' + icon);
    }
  }
}
