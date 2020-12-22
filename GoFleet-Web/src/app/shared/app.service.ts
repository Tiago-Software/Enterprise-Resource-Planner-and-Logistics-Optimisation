import { environment } from './../../environments/environment.prod';
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, from } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AppService {

  private appPath = environment.apiUrl
  constructor(private http:HttpClient) 
  {

  }

  create(data): Observable<any>
  {
    return this.http.post<any>('','')
  }
}
