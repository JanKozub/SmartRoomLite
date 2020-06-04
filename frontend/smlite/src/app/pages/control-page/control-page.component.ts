import {AfterViewInit, Component, OnInit} from '@angular/core';
import {PropertiesService} from "../../../services/properties.service";
import {interval} from "rxjs";
import {ColorService} from "../../../services/color.service";

@Component({
  selector: 'app-control-page',
  templateUrl: './control-page.component.html',
  styleUrls: ['./control-page.component.sass']
})
export class ControlPageComponent implements AfterViewInit {

  constructor(private colorService: ColorService, private propertiesService: PropertiesService) {
  }

  ngAfterViewInit() {
    this.refreshPage();
    interval(2000).subscribe(() => {
      console.debug('updating properties');
      this.refreshPage();
    });
  }

  private refreshPage() {
    this.propertiesService.getControlProperties().subscribe(data => {
      this.colorService.setColor(data['speakers'], 'volume');
      this.colorService.setColor(data['gate'], 'gate');
    }, error => console.log(error));
  }
}
