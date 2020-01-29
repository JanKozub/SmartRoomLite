import {Component, Input, OnInit} from '@angular/core';

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

  constructor() {
  }

  ngOnInit() {
  }

}
