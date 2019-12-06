import {Component, OnInit} from '@angular/core';
import {DataService} from "../services/data.service";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.sass']
})
export class AppComponent implements OnInit {

  constructor(private dataService: DataService) {
  }

  ngOnInit() {
    this.updateStates();
  }

  updateStates(): void {
    this.dataService.updateStates();
  }
}
