import {Component, OnInit} from '@angular/core';
import {NgxMaterialTimepickerTheme} from 'ngx-material-timepicker';
import {SettingsService} from '../../../services/settings.service';

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

  private setBlindsState = false;
  private toggleClockState = false;
  private lockDoorState = false;
  private toggleDoorScreenState = false;

  private nightModeToggleHour: string;

  constructor(private settingsService: SettingsService) {
  }

  ngOnInit() {
    //GETTING CHECKBOX STATES
    this.settingsService.getProperty('blinds.morning_toggle')
      .subscribe(data => this.setBlindsState = data['value'] == 'true',
        error => console.log(error));
    this.settingsService.getProperty('clock.morning_toggle')
      .subscribe(data => this.toggleClockState = data['value'] == 'true',
        error => console.log(error));
    this.settingsService.getProperty('door.lock_on_nightMode')
      .subscribe(data => this.lockDoorState = data['value'] == 'true',
        error => console.log(error));
    this.settingsService.getProperty('door.morning_screen_toggle')
      .subscribe(data => this.toggleDoorScreenState = data['value'] == 'true',
        error => console.log(error));

    //GET INPUT STATE

    this.settingsService.getProperty('nightMode.toggle_hour')
      .subscribe(data => this.nightModeToggleHour = data['value'],
        error => console.log(error));
  }

  changeValue(property: String, value: String) {
    this.settingsService.setProperty(property, value);
  }
}
