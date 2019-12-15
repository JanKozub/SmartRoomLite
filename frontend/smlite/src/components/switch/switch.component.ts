import {Component, Input, OnInit} from '@angular/core';
import {SwitchService} from '../../services/switch.service';

@Component({
  selector: 'app-switch',
  templateUrl: './switch.component.html',
  styleUrls: ['./switch.component.sass']
})
export class SwitchComponent implements OnInit {
  @Input() type: string;

  private icon: string;

  constructor(private dataService: SwitchService) {
  }

  ngOnInit() {
    if (this.type === "light") {
      this.icon = "lightbulb";
    } else {
      if (this.type === "clock") {
        this.icon = this.type;
      } else {
        if (this.type === "lock") {
          this.icon = this.type;
        } else {
          if (this.type === "night-mode") {
            this.icon = "moon-o"
          } else {
            this.icon = "vaadin-h"
          }
        }
      }
    }
  }

  changeState(): void {
    this.dataService.changeState(this.type);
  }
}
