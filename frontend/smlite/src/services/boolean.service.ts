import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
// @ts-ignore
import properties from '../assets/properties.json';

@Injectable({
  providedIn: 'root'
})
export class BooleanService {

  private url: string = properties.baseUrl;

  constructor(private http: HttpClient) {
  }

  getState(serviceType: String) {
    let url: String = this.url + serviceType + "/getState";
    return this.http.get(`${url}`);
  }

  changeState(serviceType: String): Observable<Object> {
    console.log(this.url);
    let url: String = this.url + serviceType + "/setState";
    return this.http.post(`${url}`, "CHANGE");
  }
}
