import {Component, Input, OnInit} from '@angular/core';
import {BlindsService} from '../../../services/blinds.service';

@Component({
  selector: 'app-vertical-controller',
  templateUrl: './vertical-controller.component.html',
  styleUrls: ['./vertical-controller.component.sass']
})
export class VerticalControllerComponent implements OnInit {

  @Input()
  private type: string;

  @Input()
  private id: string;

  @Input()
  private icon: string;

  private position: number;

  constructor(private blindsService: BlindsService) {
  }

  ngOnInit() {
    this.getPosition(this.id);
  }

  getPosition(serviceType: String) {
    this.blindsService.getPosition(serviceType.toLowerCase().charAt(serviceType.length - 1))
      .subscribe(data => this.position = Number(data),
        error => console.log(error));
  }

  goUp() {
    if (this.position < 5) {
      this.position = this.position + 1;
      this.blindsService.setPosition(this.id, String(this.position));
    }
  }

  goDown() {
    if (this.position > 1) {
      this.position = this.position - 1;
      this.blindsService.setPosition(this.id, String(this.position));
    }
  }
}
