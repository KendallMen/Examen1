import { CommonModule } from '@angular/common';
import { Component, computed, inject, OnInit, signal } from '@angular/core';

import { CitaService } from '../services/cita.service';
import { CitaMedica, NivelPrioridad } from '../models/cita.model';

type FiltroPrioridad = 'TODAS' | NivelPrioridad;

type EstadoCita = 'PENDIENTE' | 'EN_ATENCION' | 'ATENDIDA' | 'CANCELADA';

@Component({
  selector: 'app-cita-list',
  imports: [CommonModule],
  templateUrl: './cita-list.component.html',
  styleUrl: './cita-list.component.css'
})
export class CitaListComponent implements OnInit {
  private readonly citaService = inject(CitaService);

  readonly citas = signal<CitaMedica[]>([]);
  readonly filtroPrioridad = signal<FiltroPrioridad>('TODAS');
  readonly cargando = signal(true);
  readonly error = signal('');
  readonly citasFiltradas = computed(() => {
    const prioridad = this.filtroPrioridad();
    return prioridad === 'TODAS'
      ? this.citas()
      : this.citas().filter((cita) => cita.nivelPrioridad === prioridad);
  });

  readonly filtros: FiltroPrioridad[] = ['TODAS', 'ALTA', 'MEDIA', 'BAJA'];

  ngOnInit(): void {
    this.cargarCitas();
  }

  seleccionarPrioridad(prioridad: FiltroPrioridad): void {
    this.filtroPrioridad.set(prioridad);
  }

  avanzarEstado(cita: CitaMedica): void {
    const siguienteEstado = this.siguienteEstado(cita.estado);
    if (!siguienteEstado) {
      return;
    }

    this.citaService.actualizarEstado(cita.id, siguienteEstado).subscribe({
      next: (citaActualizada) => {
        this.citas.update((citas) =>
          citas.map((actual) => actual.id === cita.id ? citaActualizada : actual)
        );
      },
      error: () => this.error.set('No se pudo actualizar el estado de la cita.')
    });
  }

  etiquetaEstado(estado: string): string {
    return estado.replace('_', ' ');
  }

  puedeAvanzar(estado: string): boolean {
    return estado === 'PENDIENTE' || estado === 'EN_ATENCION';
  }

  private cargarCitas(): void {
    this.citaService.getCitas().subscribe({
      next: (citas) => {
        this.citas.set(citas);
        this.cargando.set(false);
      },
      error: () => {
        this.error.set('No se pudieron cargar las citas.');
        this.cargando.set(false);
      }
    });
  }

  private siguienteEstado(estado: string): EstadoCita | null {
    if (estado === 'PENDIENTE') {
      return 'EN_ATENCION';
    }
    if (estado === 'EN_ATENCION') {
      return 'ATENDIDA';
    }
    return null;
  }
}
