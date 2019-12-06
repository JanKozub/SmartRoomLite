import {Component, OnInit} from '@angular/core';
import '@vaadin/vaadin-icons';
import {ServiceType} from "../../../services/serviceType";
import {Switch} from "../switch";
import {DataService} from "../../../services/data.service";

@Component({
  selector: 'app-light-switch',
  templateUrl: './light-switch.component.html',
  styleUrls: ['./light-switch.component.sass']
})
export class LightSwitchComponent implements OnInit, Switch {

  constructor(private dataService: DataService) {
  }

  ngOnInit() {
  }

  getState(): void {
    this.dataService.updateState(ServiceType.Light.toString());
  }

  changeState(): void {
    this.dataService.changeState(ServiceType.Light);
  }
}
