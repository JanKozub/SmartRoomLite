import {Injectable} from "@angular/core";
import {BooleanService} from "./boolean.service";
import {ServiceType} from "./serviceType";
// @ts-ignore
import * as $ from 'jquery';

@Injectable({
  providedIn: 'root'
})
export class DataService {

  constructor(private booleanService: BooleanService) {
  }

  updateState(serviceType: String) {
    let className: String = '.icon--' + serviceType.toLowerCase();
    this.booleanService.getState(serviceType.toLowerCase())
      .subscribe(data =>
          this.setColor(data, className),
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

  changeState(serviceType: ServiceType) {
    let className: String = '.icon--' + serviceType;
    this.booleanService.changeState(serviceType)
      .subscribe(data =>
          this.setColor(data, className),
        error => console.log(error));
  }

  setColor(data: Object, className: String) {
    if (data) $(className).css("color", "#ff8b00");
    else $(className).css("color", "rgba(0,0,0,0.46)");
  }
}
