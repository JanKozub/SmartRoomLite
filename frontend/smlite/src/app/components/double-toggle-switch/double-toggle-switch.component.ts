import {Component, Input, OnInit} from '@angular/core';
import {SwitchService} from '../../../services/switch.service';

@Component({
  selector: 'app-double-toggle-switch',
  templateUrl: './double-toggle-switch.component.html',
  styleUrls: ['./double-toggle-switch.component.sass']
})
export class DoubleToggleSwitchComponent implements OnInit {

  @Input()
  private iconLeft: string;

  @Input()
  private iconRight: string;

  constructor(private switchService: SwitchService) {
  }

  ngOnInit() {
  }

}
