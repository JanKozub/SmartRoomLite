import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
// @ts-ignore
import properties from 'src/assets/properties.json';
import {Observable} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class PropertiesService {

  private url: string = properties.baseUrl;

  constructor(private http: HttpClient) {
  }

  getSwitchesProperties(): Observable<any> {
    return this.http.get(this.url + '/properties/getProperties/switches');
  }

  getControlProperties(): Observable<any> {
    return this.http.get(this.url + '/properties/getProperties/control');
  }

  getSettingsProperties(): Observable<any> {
    return this.http.get(this.url + '/properties/getProperties/settings');
  }

  setProperty(property: string, value: string): void {
    console.log('changing property', property, ' to ', value);
    this.http.post(this.url + '/settings/setProperty/' + property, value)
      .subscribe(() => {
      }, () => console.error('Couldn\'t set properties(properties.service:28)!'));
  }
}
