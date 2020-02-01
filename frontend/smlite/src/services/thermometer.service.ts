import {Injectable} from '@angular/core';
// @ts-ignore
import properties from '../assets/properties.json';
import {HttpClient} from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class ThermometerService {

  private url: string = properties.baseUrl;

  constructor(private http: HttpClient) {
  }

  getTemperature() {
    console.log('updating temperature');
    return this.http.get(this.url + '/thermometer/getProp/getTemperature');
  }

  getHumidity() {
    console.log('updating humidity');
    return this.http.get(this.url + '/thermometer/getProp/getHumidity');
  }

  toggleThermometer() {
    console.log('toggling thermometer');
    return this.http.post(this.url + '/thermometer/toggle', 'toggle')
      .subscribe(() => {
      }, error => console.log(error));
  }
}
