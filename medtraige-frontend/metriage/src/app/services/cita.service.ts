import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';

import { environment } from '../../environments/environment';
import {
  CitaMedica,
  CrearCitaPayload,
  NivelPrioridad
} from '../models/cita.model';

@Injectable({ providedIn: 'root' })
export class CitaService {

  private readonly http = inject(HttpClient);

  getCitas(): Observable<CitaMedica[]> {
    return this.http.get<CitaMedica[]>(`${environment.API_URL}citas`);
  }

  getCitasPorPrioridad(prioridad: NivelPrioridad): Observable<CitaMedica[]> {
    return this.http.get<CitaMedica[]>(
      `${environment.API_URL}citas/prioridad/${prioridad}`
    );
  }

  crearCita(payload: CrearCitaPayload): Observable<CitaMedica> {
    return this.http.post<CitaMedica>(`${environment.API_URL}citas`, payload);
  }

  actualizarEstado(id: number, estado: string): Observable<CitaMedica> {
    return this.http.patch<CitaMedica>(
      `${environment.API_URL}citas/${id}/estado`,
      null,
      { params: { nuevoEstado: estado } }
    );
  }

}

