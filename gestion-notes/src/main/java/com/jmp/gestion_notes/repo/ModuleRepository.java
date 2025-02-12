package com.jmp.gestion_notes.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.jmp.gestion_notes.model.Module;


public interface ModuleRepository extends JpaRepository<Module, Long> {

	Module findByTitre(String titre);
	Module findByCode(String code);
	List<Module> findByNiveauId(Long niveauId);
}
