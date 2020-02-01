import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
// @ts-ignore
import properties from '../assets/properties.json';
import {ColorService} from './color.service';

@Injectable({
  providedIn: 'root'
})
export class SwitchService {

  private url: string = properties.baseUrl;

  constructor(private http: HttpClient, private colorService: ColorService) {
  }

  changeState(serviceType: String) {
    console.log('changing state of', serviceType);
    return this.http.post(this.url + '/switch/setState', serviceType.toLowerCase())
      .subscribe(data => this.colorService.setColor(data, serviceType.toLowerCase()),
        error => console.log(error));
  }

  toggleThermometer() {
    return this.http.post(this.url + '/thermometer/toggle', 'toggle')
      .subscribe(() => {
      }, error => console.log(error));
  }
}
