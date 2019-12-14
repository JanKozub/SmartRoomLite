import {Injectable} from "@angular/core";
import {ServiceType} from "./serviceType";
// @ts-ignore
import * as $ from 'jquery';
import {HttpClient} from "@angular/common/http";
// @ts-ignore
import properties from "../assets/properties.json";

@Injectable({
  providedIn: 'root'
})
export class DataService {

  private url: string = properties.baseUrl;

  constructor(private http: HttpClient) {
  }

  updateState(serviceType: String) {
    this.http.get(this.url + "/switch/getState/" + serviceType)
      .subscribe(data =>
          this.setColor(data, '.icon--' + serviceType),
        error => console.log(error));
  }

  updateStates() {
    for (let serviceType in ServiceType) {
      if (isNaN(Number(serviceType))) {
        if (serviceType !== "Blind1" && serviceType !== "Blind2")
          this.updateState(serviceType);
      }
    }
  }

  changeState(serviceType: String) {
    this.http.post(this.url + "/switch/setState", serviceType)
      .subscribe(data =>
          this.setColor(data, '.icon--' + serviceType),
        error => console.log(error));
  }

  setColor(data: Object, className: String) {
    if (data) $(className).css("color", "#ff8b00");
    else $(className).css("color", "rgba(0,0,0,0.46)");
  }
}
