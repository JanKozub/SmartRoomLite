import {Component, Input, OnInit} from '@angular/core';
import {BlindsService} from 'src/app/services/blinds.service';

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
  public icon: string;

  @Input()
  public value: string;

  constructor(private blindsService: BlindsService) {
  }

  ngOnInit(): void {
    this.getPosition(this.id);
  }

  getPosition(serviceType: string): void {
    this.blindsService.getPosition(serviceType.toLowerCase().charAt(serviceType.length - 1))
      .subscribe(data => this.value = String(data),
        () => console.warn('Couldn\'t get position of Blind!'));
  }

  onChange(): void {
    this.blindsService.setPosition(this.id, String(this.value));
  }
}
