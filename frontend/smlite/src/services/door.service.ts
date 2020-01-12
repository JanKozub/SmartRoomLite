import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
// @ts-ignore
import properties from '../assets/properties.json';

@Injectable({
  providedIn: 'root'
})
export class DoorService {

  lockState: boolean;
  screenState: boolean;

  private url: string = properties.baseUrl;

  constructor(private http: HttpClient) {
  }

  getState(device: String) {
    console.log('updating state of', device);
    return this.http.get(this.url + '/switch/getState/' + device)
      .subscribe(data => {
        if (device === 'lock') {
          this.lockState = Boolean(data);
        } else {
          this.screenState = Boolean(data);
        }
      }, error => console.log(error));
  }

  toggle(device: String) {
    console.log('changing state of', device);
    return this.http.post(this.url + '/switch/setState', device.toLowerCase())
      .subscribe(() => {
          this.getState(device);
        },
        error => console.log(error));
  }
}
