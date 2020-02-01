import {Injectable} from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class ColorService {

  constructor() {
  }

  setColor(data: Object, type: String) {
    if (data != null && document.getElementById('icon-' + type) != null) {
      if (data) {
        document.getElementById('icon-' + type).style.color = '#ff8b00';
      } else {
        document.getElementById('icon-' + type).style.color = '#111111';
      }
    }
  }
}
