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

  private temperature: string;
  private humidity: string;

  constructor(private colorService: ColorService, private propertiesService: PropertiesService, private switchService: SwitchService) {
  }

  private static checkUrl(url: string): Boolean {
    return document.location.href.split("/").pop() === url;
  }

  private refreshSwitches() {
    this.propertiesService.getSwitchesProperties().subscribe(data => {
      this.colorService.setColor(data['light'], 'light');
      this.colorService.setColor(data['clock'], 'clock');
      this.colorService.setColor(data['lock'], 'door');
      this.colorService.setColor(data['doorScreen'], 'screen');
      this.colorService.setColor(data['nightMode'], 'night-mode');
      this.temperature = data['temp'];
      this.humidity = data['hum'];
    }, error => console.log(error));
  }

  private refreshControl() {
    this.propertiesService.getControlProperties().subscribe(data => {
      this.colorService.setColor(data['speakers'], 'speakers');
      this.colorService.setColor(data['gate'], 'gate');
    }, error => console.log(error));
  }

  ngOnInit() {
    this.updateColors();

    interval(2000).subscribe(() => {
      this.updateColors();
    });
  }

  public toggleThermometer() {
    this.switchService.changeState('thermometer');
  }

  public getRouterOutletState(outlet) {
    return outlet.isActivated ? outlet.activatedRoute : '';
  }

  private updateColors(): void {
    console.debug('updating properties');
    if (AppComponent.checkUrl("switches")) {
      this.refreshSwitches();
    } else {
      if (AppComponent.checkUrl("control")) {
        this.refreshControl();
      }
    }
  }
}
