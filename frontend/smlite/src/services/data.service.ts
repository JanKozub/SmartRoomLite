import {Injectable} from "@angular/core";
import {BooleanService} from "./boolean.service";
import {ServiceType} from "./serviceType";
import * as $ from 'jquery';

@Injectable({
  providedIn: 'root'
})
export class DataService {

  constructor(private booleanService: BooleanService) {
  }

  updateState(serviceType: ServiceType) {
    let className: String = '.icon--' + serviceType;
    this.booleanService.getState(serviceType)
      .subscribe(data =>
          this.setColor(data, className),
        error => console.log(error));
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
