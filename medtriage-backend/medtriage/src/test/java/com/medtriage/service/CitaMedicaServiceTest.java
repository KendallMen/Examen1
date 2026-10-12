package com.medtriage.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.medtriage.dto.CitaMedicaDTO;
import com.medtriage.dto.CrearCitaDTO;
import com.medtriage.model.Doctor;
import com.medtriage.model.Paciente;
import com.medtriage.repository.CitaMedicaRepository;
import com.medtriage.repository.DoctorRepository;
import com.medtriage.repository.PacienteRepository;

@ExtendWith(MockitoExtension.class)
class CitaMedicaServiceTest {

    @Mock
    private CitaMedicaRepository citaMedicaRepository;

    @Mock
    private DoctorRepository doctorRepository;

    @Mock
    private PacienteRepository pacienteRepository;

    @InjectMocks
    private CitaMedicaService citaMedicaService;

    @Test
    void crearCitaCreaCitaPendienteConCodigoUnico() {
        Paciente paciente = new Paciente();
        paciente.setNombreCompleto("Ana Perez");

        Doctor doctor = new Doctor();
        doctor.setNombre("Dr. Carlos Mora");
        doctor.setDisponible(true);

        CrearCitaDTO request = new CrearCitaDTO();
        request.setPacienteId(1L);
        request.setDoctorId(2L);
        request.setNivelPrioridad(CrearCitaDTO.NivelPrioridad.ALTA);
        request.setMotivoConsulta("Fiebre");
        request.setMontoConsulta(new BigDecimal("25000.00"));

        when(pacienteRepository.findById(1L)).thenReturn(Optional.of(paciente));
        when(doctorRepository.findById(2L)).thenReturn(Optional.of(doctor));
        when(citaMedicaRepository.existsByCodigoCita(any(String.class))).thenReturn(false);
        when(citaMedicaRepository.save(any()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        CitaMedicaDTO result = citaMedicaService.crearCita(request);

        assertTrue(result.getCodigoCita().matches("CIT-2026-\\d{4}"));
        assertEquals("ALTA", result.getNivelPrioridad());
        assertEquals("PENDIENTE", result.getEstado());
    }
}
