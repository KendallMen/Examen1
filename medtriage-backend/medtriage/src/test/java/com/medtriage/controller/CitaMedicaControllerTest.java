package com.medtriage.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.medtriage.dto.CitaMedicaDTO;
import com.medtriage.service.CitaMedicaService;

@ExtendWith(MockitoExtension.class)
class CitaMedicaControllerTest {

    @Mock
    private CitaMedicaService citaMedicaService;

    @InjectMocks
    private CitaMedicaController citaMedicaController;

    @Test
    void obtenerTodasDevuelveLasCitas() {
        List<CitaMedicaDTO> citas = List.of(new CitaMedicaDTO());
        when(citaMedicaService.obtenerTodas()).thenReturn(citas);

        assertEquals(citas, citaMedicaController.obtenerTodas());
        verify(citaMedicaService).obtenerTodas();
    }

    @Test
    void crearCitaDevuelveEstadoCreated() {
        CitaMedicaDTO cita = new CitaMedicaDTO();
        when(citaMedicaService.crearCita(org.mockito.ArgumentMatchers.any())).thenReturn(cita);

        ResponseEntity<CitaMedicaDTO> response = citaMedicaController.crearCita(null);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(cita, response.getBody());
    }
}
