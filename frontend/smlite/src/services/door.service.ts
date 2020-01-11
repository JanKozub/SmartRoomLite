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

  getLockState() {
    console.log('updating state of lock');
    return this.http.get(this.url + '/switch/getState/door')
      .subscribe(data => {
        this.lockState = Boolean(data);
      }, error => console.log(error));
  }

  getScreenState() {
    console.log('updating state of lock screen');
    return this.http.get(this.url + '/switch/getState/door/door_screen')
      .subscribe(data => {
        this.screenState = Boolean(data);
      }, error => console.log(error));
  }
}
