import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { DailyControl, ExtraExpense } from '../models/dailyControl.model';
import { DashboardSummary } from '../models/dashboard.model';
import { environment } from '../../../environments/environment';

export interface DailyControlRequest {
  faturamento: number;
  gasolina: number;
  comida: number;
  observation?: string;
  extras?: ExtraExpense[];
}

@Injectable({
  providedIn: 'root'
})
export class DailyControlService {

  // Caso o backend também tenha sido renomeado,
  // altere "transactions" para o novo endpoint.
  private readonly api = `${environment.apiUrl}/transactions`;

  constructor(private http: HttpClient) {}

  /**
   * Lista todos os controles diários.
   */
  getAll(): Observable<DailyControl[]> {
    return this.http.get<DailyControl[]>(this.api);
  }

  /**
   * Retorna os registros mais recentes.
   */
  getRecent(limit = 5): Observable<DailyControl[]> {
    return this.http.get<DailyControl[]>(
      `${this.api}/recent?limit=${limit}`
    );
  }

  /**
   * Obtém um controle diário pelo ID.
   */
  getById(id: number): Observable<DailyControl> {
    return this.http.get<DailyControl>(`${this.api}/${id}`);
  }

  /**
   * Cria um novo controle diário.
   */
  create(control: DailyControlRequest): Observable<DailyControl> {
    return this.http.post<DailyControl>(
      this.api,
      control
    );
  }

  /**
   * Atualiza um controle diário existente.
   */
  update(id: number, control: DailyControlRequest): Observable<DailyControl> {
    return this.http.put<DailyControl>(
      `${this.api}/${id}`,
      control
    );
  }

  /**
   * Remove um controle diário.
   */
  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.api}/${id}`);
  }

  /**
   * Retorna o resumo do dashboard.
   * Remova este método caso o dashboard passe a calcular
   * tudo no frontend.
   */
  getSummary(): Observable<DashboardSummary> {
    return this.http.get<DashboardSummary>(
      `${this.api}/dashboard/summary`
    );
  }
}
