import {Component, OnInit} from '@angular/core';
import {fadeAnimation} from '../animations/fade.animation';
import {ThermometerService} from '../services/thermometer.service';
import {interval} from 'rxjs';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.sass'],
  animations: [fadeAnimation]
})
export class AppComponent implements OnInit {

  private temperature: string;
  private humidity: string;

  constructor(private thermometerService: ThermometerService) {
    console.log('updating values');
    // switchService.updateStates();
    interval(5000).subscribe(() => {
      this.thermometerService.getTemperature()
        .subscribe(data => this.temperature = String(data['value']).slice(0, -1),
          error => console.log(error));
      this.thermometerService.getHumidity()
        .subscribe(data => this.humidity = data['value'],
          error => console.log(error));
    });
  }

  ngOnInit() {
  }

  public toggleThermometer() {
    this.thermometerService.toggleThermometer();
  }

  public getRouterOutletState(outlet) {
    return outlet.isActivated ? outlet.activatedRoute : '';
  }
}
