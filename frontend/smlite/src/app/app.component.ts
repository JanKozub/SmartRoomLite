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

  ngOnInit() {
    this.refreshSwitches();
    interval(2000).subscribe(() => {
      console.debug('updating properties');
      this.refreshSwitches();
    });
  }

  public toggleThermometer() {
    this.switchService.changeState('thermometer');
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

  public getRouterOutletState(outlet) {
    return outlet.isActivated ? outlet.activatedRoute : '';
  }
}
