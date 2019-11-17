import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {ServiceType} from "./serviceType";
import properties from '../assets/properties.json';

@Injectable({
  providedIn: 'root'
})
export class BlindsService {

  private baseUrl: string = properties.baseUrl;

  constructor(private http: HttpClient) {
  }

  getPosition(serviceType: ServiceType) {
    let url: string = this.baseUrl + serviceType.toString() + "/getPosition";
    return this.http.get(`${url}`);
  }

  setPosition(serviceType: ServiceType, position: string): Observable<Object> {
    let url: String = this.baseUrl + serviceType.toString() + "/setPosition";
    return this.http.post(`${url}`, position);
  }
}
