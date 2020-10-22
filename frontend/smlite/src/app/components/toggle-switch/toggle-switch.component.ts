import {Component, Input} from '@angular/core';
import {SwitchService} from 'src/app/services/switch.service';

@Component({
  selector: 'app-toggle-switch',
  templateUrl: './toggle-switch.component.html',
  styleUrls: ['./toggle-switch.component.sass']
})
export class ToggleSwitchComponent {

  @Input()
  public type: string;

  @Input()
  public icon: string;

  constructor(private switchService: SwitchService) {
  }

  changeState(): void {
    this.switchService.changeState(this.type);
  }
}
