package com.medtriage.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.medtriage.dto.CitaMedicaDTO;
import com.medtriage.dto.CrearCitaDTO;
import com.medtriage.model.CitaMedica;
import com.medtriage.model.Doctor;
import com.medtriage.model.Paciente;
import com.medtriage.repository.CitaMedicaRepository;
import com.medtriage.repository.DoctorRepository;
import com.medtriage.repository.PacienteRepository;

@Service
@Transactional
public class CitaMedicaService {

	private static final String ESTADO_PENDIENTE = "PENDIENTE";

	private final CitaMedicaRepository citaMedicaRepository;
	private final DoctorRepository doctorRepository;
	private final PacienteRepository pacienteRepository;

	public CitaMedicaService(CitaMedicaRepository citaMedicaRepository,
			DoctorRepository doctorRepository, PacienteRepository pacienteRepository) {
		this.citaMedicaRepository = citaMedicaRepository;
		this.doctorRepository = doctorRepository;
		this.pacienteRepository = pacienteRepository;
	}

	@Transactional(readOnly = true)
	public List<CitaMedicaDTO> obtenerTodas() {
		return citaMedicaRepository.findAll().stream()
				.map(this::convertirADTO)
				.toList();
	}

	@Transactional(readOnly = true)
	public List<CitaMedicaDTO> obtenerPorPrioridad(String prioridad) {
		return citaMedicaRepository.findByNivelPrioridadOrderByFechaCitaDesc(prioridad).stream()
				.map(this::convertirADTO)
				.toList();
	}

	public CitaMedicaDTO crearCita(CrearCitaDTO dto) {
		Paciente paciente = pacienteRepository.findById(dto.getPacienteId())
				.orElseThrow(() -> new IllegalArgumentException("Paciente no encontrado"));
		Doctor doctor = doctorRepository.findById(dto.getDoctorId())
				.orElseThrow(() -> new IllegalArgumentException("Doctor no encontrado"));

		if (!doctor.isDisponible()) {
			throw new IllegalStateException("El doctor no está disponible");
		}

		CitaMedica cita = new CitaMedica();
		cita.setCodigoCita(generarCodigoCita());
		cita.setPaciente(paciente);
		cita.setDoctor(doctor);
		cita.setNivelPrioridad(dto.getNivelPrioridad().name());
		cita.setMotivoConsulta(dto.getMotivoConsulta());
		cita.setMontoConsulta(dto.getMontoConsulta());
		cita.setEstado(ESTADO_PENDIENTE);
		cita.setFechaCita(LocalDateTime.now());

		return convertirADTO(citaMedicaRepository.save(cita));
	}

	public CitaMedicaDTO actualizarEstado(Long id, String nuevoEstado) {
		if (!List.of("EN_ATENCION", "ATENDIDA", "CANCELADA").contains(nuevoEstado)) {
			throw new IllegalArgumentException("Estado no permitido");
		}

		CitaMedica cita = citaMedicaRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Cita no encontrada"));
		cita.setEstado(nuevoEstado);
		return convertirADTO(citaMedicaRepository.save(cita));
	}

	private String generarCodigoCita() {
		String codigo;
		do {
			int numero = ThreadLocalRandom.current().nextInt(0, 10000);
			codigo = String.format("CIT-2026-%04d", numero);
		} while (citaMedicaRepository.existsByCodigoCita(codigo));
		return codigo;
	}

	private CitaMedicaDTO convertirADTO(CitaMedica cita) {
		CitaMedicaDTO dto = new CitaMedicaDTO();
		dto.setId(cita.getId());
		dto.setCodigoCita(cita.getCodigoCita());
		dto.setNombrePaciente(cita.getPaciente().getNombreCompleto());
		dto.setNombreDoctor(cita.getDoctor().getNombre());
		dto.setNivelPrioridad(cita.getNivelPrioridad());
		dto.setMotivoConsulta(cita.getMotivoConsulta());
		dto.setEstado(cita.getEstado());
		dto.setMontoConsulta(cita.getMontoConsulta());
		dto.setFechaCita(cita.getFechaCita());
		return dto;
	}
}
