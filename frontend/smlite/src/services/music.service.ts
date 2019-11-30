import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
// @ts-ignore
import properties from "../assets/properties.json";

@Injectable({
  providedIn: 'root'
})
export class MusicService {

  private url: string = properties.baseUrl;

  constructor(private http: HttpClient) {
  }

  getMusicState() {
    return this.http.get(`${this.url}/state`)
  }

  changeMusicState(): Observable<Object> {
    return this.http.post(`${this.url}`, "CHANGE");
  }

}
