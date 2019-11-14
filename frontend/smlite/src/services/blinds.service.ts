import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {ServiceType} from "./serviceType";

@Injectable({
  providedIn: 'root'
})
export class BlindsService {

  private baseUrl: string = 'http://localhost:8080/smlite-rest/';

  // private baseUrl: string = 'http://10.0.98.125:8080/smlite-rest/';

  constructor(private http: HttpClient) {
  }

  getPosition(serviceType: ServiceType) {
    let url: string = this.baseUrl + serviceType.toString() + "/getState";
    return this.http.get(`${url}`);
  }

  setPosition(serviceType: ServiceType, position: string): Observable<Object> {
    let url: String = this.baseUrl + serviceType.toString() + "/setState";
    return this.http.post(`${url}`, position);
  }
}
