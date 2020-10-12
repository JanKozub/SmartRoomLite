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

  getPosition(serviceType: string) {
    console.debug('getting value of blind', serviceType);
    const url: string = this.baseUrl + '/blind/getPosition/blind' + serviceType;
    return this.http.get(`${url}`);
  }

  setPosition(serviceType: string, position: string) {
    console.log('Blind, id=', serviceType, ', newState=', position);
    const url: string = this.baseUrl + '/blind/setPosition/blind' + serviceType;
    return this.http.post(`${url}`, position)
      .subscribe((data) => {
          if (data) {
            this.getPosition(serviceType);
          }
        },
        () => console.error('Error occurred while setting new blind state! Check your network connection.'));
  }
}
