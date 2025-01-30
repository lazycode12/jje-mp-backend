package com.jmp.gestion_notes.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jmp.gestion_notes.model.Matiere;

public interface MatiereRepository extends JpaRepository<Matiere, Long> {

	List<Matiere> findByModuleId(Long moduleId);
}
