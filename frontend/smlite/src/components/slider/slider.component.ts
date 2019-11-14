import {Component, OnInit} from '@angular/core';
import {BlindsService} from "../../services/blinds.service";
import {ServiceType} from "../../services/serviceType";

@Component({
  selector: 'app-slider',
  templateUrl: './slider.component.html',
  styleUrls: ['./slider.component.sass']
})
export class SliderComponent implements OnInit{
  value = 0;

  constructor(private blindsService: BlindsService) {
  }

  ngOnInit() {

  }

  getPosition(): void {
    this.blindsService.getPosition(ServiceType.Blind1);
  }

  setPosition(): void {
    this.blindsService.setPosition(ServiceType.Blind2, this.value.toString());
  }
}
