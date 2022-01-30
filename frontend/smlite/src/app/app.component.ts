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

  public temperature = '20';
  public humidity = '20';

  constructor(private propertiesService: PropertiesService, private colorService: ColorService) {
  }

  private static checkUrl(url: string): boolean {
    return document.location.href.split('/').pop() === url;
  }

  public refreshSwitches(): void {
    this.propertiesService.getSwitchesProperties().subscribe(data => {
      this.colorService.setColor(data['light'], 'light');
      this.colorService.setColor(data['clock'], 'clock');
      this.temperature = Math.floor(data['temp']).toString();
      this.humidity = Math.floor(data['hum']).toString();
    }, () => console.warn('Couldn\'t refresh switches! Most likely network error :('));
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
    }
  }
}

