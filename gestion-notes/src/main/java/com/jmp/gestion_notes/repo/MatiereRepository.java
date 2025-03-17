package com.jmp.gestion_notes.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jmp.gestion_notes.model.Matiere;

public interface MatiereRepository extends JpaRepository<Matiere, Long> {

	Optional<List<Matiere>> findByModuleId(Long moduleId);
	Matiere findByTitre(String titre);
}
