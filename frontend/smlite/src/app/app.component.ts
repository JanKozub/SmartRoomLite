import {Component, OnInit} from '@angular/core';
import {SwitchService} from '../services/switch.service';
import {DoorService} from '../services/door.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.sass']
})
export class AppComponent implements OnInit {

  constructor(private dataService: SwitchService, private doorService: DoorService) {
  }

  ngOnInit() {
    this.updateStates();
    this.doorService.getLockState();
    this.doorService.getScreenState();
  }

  updateStates(): void {
    this.dataService.updateStates();
  }
}
