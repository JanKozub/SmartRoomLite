import {Component, OnInit} from '@angular/core';
import {fadeAnimation} from 'src/app/animations/fade.animation';
import '@vaadin/vaadin-icons/vaadin-icons.js';
import {PropertiesService} from './services/properties.service';
import {ColorService} from './services/color.service';
import {interval} from 'rxjs';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.sass'],
  animations: [fadeAnimation]
})
export class AppComponent implements OnInit {

  title = 'smlite';

  public temperature: string;
  public humidity: string;

  constructor(private propertiesService: PropertiesService, private colorService: ColorService) {
  }

  private static checkUrl(url: string): boolean {
    return document.location.href.split('/').pop() === url;
  }

  public refreshSwitches(): void {
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

  public refreshControl(): void {
    this.propertiesService.getControlProperties().subscribe(data => {
      this.colorService.setColor(data['speakers'], 'speakers');
      this.colorService.setColor(data['thermometerScreen'], 'thermometer');
      this.temperature = data['temp'];
      this.humidity = data['hum'];
    }, e => console.warn('Couldn\'t refresh control! Most likely network error :(' + e.toString()));
  }

  ngOnInit(): void {
    this.updateColors();

    interval(2000).subscribe(() => {
      this.updateColors();
    });
  }

  public getRouterOutletState(outlet): any {
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

