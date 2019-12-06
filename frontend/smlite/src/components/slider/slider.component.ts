import {Component, Input, OnInit} from '@angular/core';
import {BlindsService} from "../../services/blinds.service";
import {ServiceType} from "../../services/serviceType";

@Component({
  selector: 'app-slider',
  templateUrl: './slider.component.html',
  styleUrls: ['./slider.component.sass']
})

export class SliderComponent implements OnInit{

  @Input() id: number;

  serviceType: ServiceType;
  value: number;

  updateValue() {
    this.setPosition(this.serviceType, this.value);
  }

  constructor(private blindsService: BlindsService) {
  }

  ngOnInit() {
    if(this.id == 1) this.serviceType = ServiceType.Blind1;
    else this.serviceType = ServiceType.Blind2;
    this.getPosition(this.serviceType);
  }

  getPosition(serviceType: ServiceType) {
    this.blindsService.getPosition(serviceType)
      .subscribe(data => this.value = Number(data),
      error => console.log(error));
  }

  setPosition(serviceType: ServiceType, newValue: number): void {
    this.blindsService.setPosition(serviceType, newValue.toString())
    .subscribe(data => console.log(),
    error => console.log(error));
  }
}
