import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
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
    let url: string = this.baseUrl + '/blind' + serviceType + '/getPosition';
    return this.http.get(`${url}`);
  }

  setPosition(serviceType: String, position: string): Observable<Object> {
    let url: String = this.baseUrl + '/blind' + serviceType + '/setPosition';
    return this.http.post(`${url}`, position);
  }
}
