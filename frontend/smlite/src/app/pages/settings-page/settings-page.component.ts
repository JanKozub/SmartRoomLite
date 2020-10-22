import {Component, OnInit} from '@angular/core';
import {NgxMaterialTimepickerTheme} from 'ngx-material-timepicker';
import {PropertiesService} from 'src/app/services/properties.service';

@Component({
  selector: 'app-settings-page',
  templateUrl: './settings-page.component.html',
  styleUrls: ['./settings-page.component.sass']
})
export class SettingsPageComponent implements OnInit {

  appTheme: NgxMaterialTimepickerTheme = {
    container: {
      bodyBackgroundColor: '#424242',
      buttonColor: '#fff'
    },
    dial: {
      dialBackgroundColor: '#555',
    },
    clockFace: {
      clockFaceBackgroundColor: '#555',
      clockHandColor: '#ff8b00',
      clockFaceTimeInactiveColor: '#fff'
    }
  };

  public setBlindsUpState = false;
  public toggleClockState = false;
  public lockDoorState = false;
  public toggleDoorScreenState = false;
  public toggleThermometerScreenState = false;

  public nightModeToggleHour: string;

  constructor(public propertiesService: PropertiesService) {
  }

  ngOnInit(): void {
    this.propertiesService.getSettingsProperties().subscribe(data => {
      this.nightModeToggleHour = data['nightModeToggleHour'];
      this.setBlindsUpState = data['setBlindsUp'];
      this.toggleClockState = data['toggleClock'];
      this.lockDoorState = data['lockDoor'];
      this.toggleDoorScreenState = data['toggleDoorScreen'];
      this.toggleThermometerScreenState = data['thermometerScreen'];
    }, () => console.warn('Couldn\'t get properties for settings! Check your connection.'));
  }

  changeValue(property: string, value: string): void {
    this.propertiesService.setProperty(property, value);
  }
}
