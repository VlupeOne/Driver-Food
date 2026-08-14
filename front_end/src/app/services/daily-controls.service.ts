import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { HttpHeaders } from '@angular/common/http';

export interface DailyControlsRequest {
  faturamento: number;
  gasolina: number;
  comida: number;
  observation: string;
}

@Injectable({
  providedIn: 'root'
})
export class DailyControlsService {

  private apiUrl = 'http://localhost:8080/dailyControls';

  constructor(private http: HttpClient) {}

  create(data: DailyControlsRequest): Observable<any> {

    const token = localStorage.getItem('token');

    const headers = new HttpHeaders({
      Authorization: `Bearer ${token}`
    });

    return this.http.post(
      `${this.apiUrl}/create`,
      data,
      { headers }
    );
  }


}
