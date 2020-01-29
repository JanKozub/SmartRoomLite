import {Component, OnInit} from '@angular/core';
import {fadeAnimation} from '../animations/fade.animation';
import {SwitchService} from '../services/switch.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.sass'],
  animations: [fadeAnimation]
})
export class AppComponent implements OnInit {

  constructor(private switchService: SwitchService) {
    switchService.updateStates();
    // interval(1000).subscribe(() => {
    //   console.log('dupa');
    // });
  }

  ngOnInit() {
  }

  public getRouterOutletState(outlet) {
    return outlet.isActivated ? outlet.activatedRoute : '';
  }
}
