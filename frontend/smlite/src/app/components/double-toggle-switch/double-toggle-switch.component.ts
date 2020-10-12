import {Component, Input, OnInit} from '@angular/core';
import {SwitchService} from '../../../services/switch.service';

@Component({
  selector: 'app-double-toggle-switch',
  templateUrl: './double-toggle-switch.component.html',
  styleUrls: ['./double-toggle-switch.component.sass']
})
export class DoubleToggleSwitchComponent implements OnInit {

  @Input()
  public iconLeft: string;

  @Input()
  public iconRight: string;

  constructor(public switchService: SwitchService) {
  }

  ngOnInit() {
  }

}
