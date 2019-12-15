import {Component, Input, OnInit} from '@angular/core';
import {BlindsService} from '../../services/blinds.service';

@Component({
  selector: 'app-slider',
  templateUrl: './slider.component.html',
  styleUrls: ['./slider.component.sass']
})

export class SliderComponent implements OnInit{

  @Input() id: number;

  serviceType: String;
  value: number;

  updateValue() {
    this.setPosition(this.serviceType, this.value);
  }

  constructor(private blindsService: BlindsService) {
  }

  ngOnInit() {
    if (this.id == 1) {
      this.serviceType = 'blind1';
    } else {
      this.serviceType = 'blind2';
    }
    this.getPosition(this.serviceType);
  }

  getPosition(serviceType: String) {
    this.blindsService.getPosition(serviceType.toLowerCase().charAt(serviceType.length - 1))
      .subscribe(data => this.value = Number(data),
        error => console.log(error));
  }

  setPosition(serviceType: String, newValue: number): void {
    this.blindsService.setPosition(serviceType.toLowerCase().charAt(serviceType.length - 1), newValue.toString())
      .subscribe(data => console.log(),
        error => console.log(error));
  }
}
