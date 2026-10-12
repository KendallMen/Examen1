import { CommonModule } from '@angular/common';
import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { finalize } from 'rxjs';

import { CrearCitaPayload, NivelPrioridad } from '../models/cita.model';
import { CitaService } from '../services/cita.service';

@Component({
  selector: 'app-cita-form',
  imports: [CommonModule, FormsModule],
  templateUrl: './cita-form.component.html',
  styleUrl: './cita-form.component.css'
})
export class CitaFormComponent {
  private readonly citaService = inject(CitaService);
  private readonly router = inject(Router);

  readonly prioridades: NivelPrioridad[] = ['ALTA', 'MEDIA', 'BAJA'];
  readonly cita: CrearCitaPayload = {
    pacienteId: 0,
    doctorId: 0,
    nivelPrioridad: 'MEDIA',
    motivoConsulta: '',
    montoConsulta: 0
  };
  enviando = false;
  error = '';

  guardar(): void {
    this.enviando = true;
    this.error = '';

    this.citaService.crearCita(this.cita).pipe(
      finalize(() => this.enviando = false)
    ).subscribe({
      next: () => this.router.navigate(['/citas']),
      error: (response) => this.error = response.error?.detail ?? 'No se pudo registrar la cita.'
    });
  }
}
