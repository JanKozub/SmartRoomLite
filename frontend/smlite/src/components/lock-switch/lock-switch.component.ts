import {Component, OnInit} from '@angular/core';
import '@vaadin/vaadin-icons';
import {Switch} from "../switch";
import {DataService} from "../../services/data.service";
import {ServiceType} from "../../services/serviceType";

@Component({
  selector: 'app-lock-switch',
  templateUrl: './lock-switch.component.html',
  styleUrls: ['./lock-switch.component.sass']
})
export class LockSwitchComponent implements OnInit, Switch {

  constructor(private dataService: DataService) {
  }

  ngOnInit() {
    this.getState();
  }

  getState(): void {
    this.dataService.updateState(ServiceType.Lock);
  }

  changeState(): void {
    this.dataService.changeState(ServiceType.Lock);
  }
}
