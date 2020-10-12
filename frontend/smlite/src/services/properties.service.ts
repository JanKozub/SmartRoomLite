import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
// @ts-ignore
import properties from '../assets/properties.json';

@Injectable({
  providedIn: 'root'
})
export class PropertiesService {

  private url: string = properties.baseUrl;

  constructor(private http: HttpClient) {
  }

  getSwitchesProperties() {
    return this.http.get(this.url + '/properties/getProperties/switches');
  }

  getControlProperties() {
    return this.http.get(this.url + '/properties/getProperties/control');
  }

  getSettingsProperties() {
    return this.http.get(this.url + '/properties/getProperties/settings');
  }

  setProperty(property: string, value: string) {
    console.log('changing property', property, ' to ', value);
    this.http.post(this.url + '/settings/setProperty/' + property, value)
      .subscribe(() => {
      }, () => console.error('Couldn\'t set properties(properties.service:28)!'));
  }
}
