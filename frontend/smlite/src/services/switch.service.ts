import {Injectable} from '@angular/core';
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
      .subscribe(data => this.setColor(data, serviceType.toLowerCase()),
        error => console.log(error));
  }

  changeState(serviceType: String) {
    console.log('changing state of', serviceType);
    return this.http.post(this.url + '/switch/setState', serviceType.toLowerCase())
      .subscribe(data => this.setColor(data, serviceType.toLowerCase()),
        error => console.log(error));
  }

  setColor(data: Object, type: String) {
    if (data) {
      document.getElementById('icon-' + type).style.color = '#ff8b00';
    } else {
      document.getElementById('icon-' + type).style.color = '#111111';
    }
  }
}
