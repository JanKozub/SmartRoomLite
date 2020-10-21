import {Component, OnInit} from '@angular/core';
import {fadeAnimation} from '../animations/fade.animation';
import {interval} from 'rxjs';
import {PropertiesService} from '../services/properties.service';
import {ColorService} from '../services/color.service';
import {SwitchService} from '../services/switch.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.sass'],
  animations: [fadeAnimation]
})
export class AppComponent implements OnInit {

  public temperature: string;
  public humidity: string;

  constructor(private colorService: ColorService, private propertiesService: PropertiesService, private switchService: SwitchService) {
  }

  private static checkUrl(url: string): boolean {
    return document.location.href.split('/').pop() === url;
  }

  public refreshSwitches() {
    this.propertiesService.getSwitchesProperties().subscribe(data => {
      this.colorService.setColor(data['light'], 'light');
      this.colorService.setColor(data['clock'], 'clock');
      this.colorService.setColor(data['lock'], 'door');
      this.colorService.setColor(data['doorScreen'], 'screen');
      this.colorService.setColor(data['nightMode'], 'night-mode');
      this.temperature = data['temp'];
      this.humidity = data['hum'];
    }, () => console.warn('Couldn\'t refresh switches! Most likely network error :('));
  }

  public refreshControl() {
    this.propertiesService.getControlProperties().subscribe(data => {
      this.colorService.setColor(data['speakers'], 'speakers');
      this.colorService.setColor(data['thermometerScreen'], 'thermometer');
      this.temperature = data['temp'];
      this.humidity = data['hum'];
    }, e => console.warn('Couldn\'t refresh control! Most likely network error :(' + e.toString()));
  }

  ngOnInit() {
    this.updateColors();

    interval(2000).subscribe(() => {
      this.updateColors();
    });
  }

  public getRouterOutletState(outlet) {
    return outlet.isActivated ? outlet.activatedRoute : '';
  }

  private updateColors(): void {
    console.debug('updating properties');
    if (AppComponent.checkUrl('switches')) {
      this.refreshSwitches();
    } else {
      if (AppComponent.checkUrl('control')) {
        this.refreshControl();
      }
    }
  }
}
