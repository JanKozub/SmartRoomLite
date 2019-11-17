import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {ServiceType} from "./serviceType";
import properties from '../assets/properties.json';

@Injectable({
  providedIn: 'root'
})
export class BooleanService {

  private url: string = properties.baseUrl;

  constructor(private http: HttpClient) {
  }

  getState(serviceType: ServiceType) {

    let url: String = this.url + serviceType.toString() + "/getState";
    return this.http.get(`${url}`);
  }

  changeState(serviceType: ServiceType): Observable<Object> {
    console.log(this.url);
    let url: String = this.url + serviceType.toString() + "/setState";
    return this.http.post(`${url}`, "CHANGE");
  }
}
