package com.medtriage.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.medtriage.model.Paciente;

public interface PacienteRepository extends JpaRepository<Paciente, Long> {
}
