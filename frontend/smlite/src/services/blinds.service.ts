import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
// @ts-ignore
import properties from '../assets/properties.json';

@Injectable({
  providedIn: 'root'
})
export class BlindsService {

  private baseUrl: string = properties.baseUrl;

  constructor(private http: HttpClient) {
  }

  getPosition(serviceType: String) {
    console.debug('getting value of blind', serviceType);
    let url: string = this.baseUrl + '/blind/getPosition/blind' + serviceType;
    return this.http.get(`${url}`);
  }

  setPosition(serviceType: String, position: string) {
    console.log('setting value of blind', serviceType, 'to', position);
    let url: String = this.baseUrl + '/blind/setPosition/blind' + serviceType;
    return this.http.post(`${url}`, position)
      .subscribe((data) => {
          if (data) {
            this.getPosition(serviceType);
          }
        },
        error => console.log(error));
  }
}
