package com.medtriage.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.medtriage.dto.CitaMedicaDTO;
import com.medtriage.dto.CrearCitaDTO;
import com.medtriage.service.CitaMedicaService;

@RestController
@RequestMapping("/api/v1/citas")
@CrossOrigin(origins = "http://localhost:4200")
public class CitaMedicaController {

    private final CitaMedicaService citaMedicaService;

    public CitaMedicaController(CitaMedicaService citaMedicaService) {
        this.citaMedicaService = citaMedicaService;
    }

    @GetMapping
    public List<CitaMedicaDTO> obtenerTodas() {
        return citaMedicaService.obtenerTodas();
    }

    @GetMapping("/prioridad/{prioridad}")
    public List<CitaMedicaDTO> obtenerPorPrioridad(@PathVariable String prioridad) {
        return citaMedicaService.obtenerPorPrioridad(prioridad);
    }

    @PostMapping
    public ResponseEntity<CitaMedicaDTO> crearCita(@RequestBody CrearCitaDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(citaMedicaService.crearCita(dto));
    }

    @PatchMapping("/{id}/estado")
    public CitaMedicaDTO actualizarEstado(@PathVariable Long id, @RequestParam String nuevoEstado) {
        return citaMedicaService.actualizarEstado(id, nuevoEstado);
    }
}
