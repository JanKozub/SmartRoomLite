import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
// @ts-ignore
import properties from '../assets/properties.json';

@Injectable({
  providedIn: 'root'
})
export class SettingsService {

  private url: string = properties.baseUrl;

  constructor(private http: HttpClient) {
  }

  getProperty(property: String) {
    console.log('updating property: ', property);
    return this.http.get(this.url + '/settings/getProperty/' + property);
  }

  setProperty(property: String, value: String) {
    console.log('changing property', property, ' to ', value);
    this.http.post(this.url + '/settings/setProperty/' + property, value)
      .subscribe(() => {
      }, error => console.log(error));
  }
}
