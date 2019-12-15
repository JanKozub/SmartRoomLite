import {Component, OnInit} from '@angular/core';
import {SwitchService} from '../services/switch.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.sass']
})
export class AppComponent implements OnInit {

  constructor(private dataService: SwitchService) {
  }

  ngOnInit() {
    this.updateStates();
  }

  updateStates(): void {
    this.dataService.updateStates();
  }
}
