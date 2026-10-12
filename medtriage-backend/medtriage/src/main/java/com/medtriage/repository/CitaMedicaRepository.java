package com.medtriage.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.medtriage.model.CitaMedica;

public interface CitaMedicaRepository extends JpaRepository<CitaMedica, Long> {

	List<CitaMedica> findByNivelPrioridadOrderByFechaCitaDesc(String nivelPrioridad);

	boolean existsByCodigoCita(String codigoCita);
}
