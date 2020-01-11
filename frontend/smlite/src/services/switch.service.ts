import {Injectable} from '@angular/core';
import * as $ from 'jquery';
import {HttpClient} from '@angular/common/http';
// @ts-ignore
import properties from '../assets/properties.json';

@Injectable({
  providedIn: 'root'
})
export class SwitchService {

  private url: string = properties.baseUrl;

  constructor(private http: HttpClient) {
  }

  updateState(serviceType: String) {
    console.log('updating state of', serviceType);
    return this.http.get(this.url + '/switch/getState/' + serviceType.toLowerCase())
      .subscribe(data =>
          this.setColor(data, '.icon--' + serviceType.toLowerCase()),
        error => console.log(error));
  }

  updateStates() {
    this.updateState('light');
    this.updateState('clock');
  }

  changeState(serviceType: String) {
    console.log('changing state of', serviceType);
    return this.http.post(this.url + '/switch/setState', serviceType.toLowerCase())
      .subscribe(data => {
          if (serviceType === 'night-mode') {
            this.updateState('light');
            this.updateState('clock');
          } else {
            this.setColor(data, '.icon--' + serviceType.toLowerCase());
          }
        },
        error => console.log(error));
  }

  setColor(data: Object, className: String) {
    if (data) {
      $(className).css('color', '#ff8b00');
    } else {
      $(className).css('color', 'rgba(0,0,0,0.46)');
    }
  }
}
