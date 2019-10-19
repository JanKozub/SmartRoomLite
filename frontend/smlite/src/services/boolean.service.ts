import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {ServiceType} from "./serviceType";

@Injectable({
  providedIn: 'root'
})
export class BooleanService {

  private baseUrl: string = 'http://localhost:8080/smlite-rest/';

  constructor(private http: HttpClient) {
  }

  getState(serviceType: ServiceType) {
    let url: String = this.baseUrl + serviceType.toString() + "/state";
    switch (serviceType) {
      case ServiceType.Light:
        return this.http.get(`${url}`);
      case ServiceType.Clock:
        return this.http.get(`${url}`);
      case ServiceType.Lock:
        return this.http.get(`${url}`);
    }
  }

  changeState(serviceType: ServiceType): Observable<Object> {
    let url: String = this.baseUrl + serviceType;
    return this.http.post(`${url}`, "CHANGE");
  }
}