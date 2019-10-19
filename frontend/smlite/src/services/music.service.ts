import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class MusicService {

  private url = 'http://localhost:8080/smlite-rest/music';

  constructor(private http: HttpClient) {
  }

  getMusicState() {
    return this.http.get(`${this.url}/state`)
  }

  changeMusicState(): Observable<Object> {
    return this.http.post(`${this.url}`, "CHANGE");
  }

}
