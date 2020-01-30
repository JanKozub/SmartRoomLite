import {Component, Input, OnInit} from '@angular/core';
import {SwitchService} from '../../../services/switch.service';

@Component({
  selector: 'app-toggle-switch',
  templateUrl: './toggle-switch.component.html',
  styleUrls: ['./toggle-switch.component.sass']
})
export class ToggleSwitchComponent implements OnInit {

  @Input()
  private type: string;

  @Input()
  private icon: string;

  constructor(private switchService: SwitchService) {
  }

  ngOnInit() {
    this.switchService.updateState(this.type);
  }

  changeState(): void {
    this.switchService.changeState(this.type);
  }
}
