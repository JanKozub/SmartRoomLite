import {Component, OnInit} from '@angular/core';
import {NgxMaterialTimepickerTheme} from 'ngx-material-timepicker';
import {PropertiesService} from '../../../services/properties.service';

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

  private setBlindsUpState = false;
  private toggleClockState = false;
  private lockDoorState = false;
  private toggleDoorScreenState = false;

  private nightModeToggleHour: string;

  constructor(private propertiesService: PropertiesService) {
  }

  ngOnInit() {
    this.propertiesService.getSettingsProperties().subscribe(data => {
      this.nightModeToggleHour = data['nightModeToggleHour'];
      this.setBlindsUpState = data['setBlindsUp'];
      this.toggleClockState = data['toggleClock'];
      this.lockDoorState = data['lockDoor'];
      this.toggleDoorScreenState = data['toggleDoorScreen'];
    }, error => console.log(error));
  }

  changeValue(property: String, value: String) {
    this.propertiesService.setProperty(property, value);
  }
}
