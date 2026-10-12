package main.java.com.medtriage.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import main.java.com.medtriage.model.CitaMedica;

public interface CitaMedicaRepository extends JpaRepository<CitaMedica, Long> {

	List<CitaMedica> findByNivelPrioridadOrderByFechaCitaDesc(String nivelPrioridad);
}
