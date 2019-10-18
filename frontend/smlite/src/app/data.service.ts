import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";

@Injectable({
  providedIn: 'root'
})
export class DataService {

  private baseUrl = 'http://localhost:8080/smlite-rest/test/state';

  constructor(private http: HttpClient) {
  }

  onClick() {
    return this.http.post(`${this.baseUrl}`, true)
  }
}
