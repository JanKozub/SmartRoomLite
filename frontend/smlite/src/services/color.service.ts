import {Injectable} from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class ColorService {

  constructor() {
  }

  setColor(data: Object, name: String) {
    if (data != null && document.getElementById('icon-' + name) != null) {
      if (data) {
        document.getElementById('icon-' + name).style.color = '#ff8b00';
      } else {
        document.getElementById('icon-' + name).style.color = '#111111';
      }
    }
  }
}
