import {Component, OnInit} from '@angular/core';
import {Switch} from "../switch";
import {DataService} from "../../../services/data.service";
import {ServiceType} from "../../../services/serviceType";

@Component({
  selector: 'app-night-mode-switch',
  templateUrl: './night-mode-switch.component.html',
  styleUrls: ['./night-mode-switch.component.sass']
})
export class NightModeSwitchComponent implements OnInit, Switch {

  constructor(private dataService: DataService) {
  }

  ngOnInit() {
    this.getState();
  }

  getState(): void {
    this.dataService.updateState(ServiceType.Light);
  }

  changeState(): void {
    this.dataService.changeState(ServiceType.Light);
  }
}
