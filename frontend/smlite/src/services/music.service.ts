import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
// @ts-ignore
import properties from '../assets/properties.json';

@Injectable({
  providedIn: 'root'
})
export class MusicService {

  private url: string = properties.baseUrl;

  constructor(private http: HttpClient) {
  }

}
