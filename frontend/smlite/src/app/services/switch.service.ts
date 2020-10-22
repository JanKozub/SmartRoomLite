import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';

// @ts-ignore
import properties from 'src/assets/properties.json';
import {ColorService} from './color.service';
import {Subscription} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class SwitchService {

  private url: string = properties.baseUrl;

  constructor(private http: HttpClient, private colorService: ColorService) {
  }

  changeState(serviceType: string): Subscription {
    console.log('switch', serviceType, 'toggled');
    return this.http.post(this.url + '/switch/setState', serviceType.toLowerCase())
      .subscribe(data => this.colorService.setColor(data, serviceType.toLowerCase()),
        () => console.error('Error occurred while toggling switch', serviceType + '.', 'Check your network connection.'));
  }
}
