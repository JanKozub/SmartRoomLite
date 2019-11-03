import {Component, OnInit} from '@angular/core';
import '@vaadin/vaadin-icons';
import {Switch} from "../switch";
import {DataService} from "../../../services/data.service";
import {ServiceType} from "../../../services/serviceType";

@Component({
  selector: 'app-clock-switch',
  templateUrl: './clock-switch.component.html',
  styleUrls: ['./clock-switch.component.sass']
})
export class ClockSwitchComponent implements OnInit, Switch {

  constructor(private dataService: DataService) {
  }

  ngOnInit() {
    this.getState();
  }

  getState(): void {
    this.dataService.updateState(ServiceType.Clock);
  }

  changeState(): void {
    this.dataService.changeState(ServiceType.Clock);
  }
}
