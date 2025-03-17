package com.jmp.gestion_notes.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jmp.gestion_notes.model.Module;
import com.jmp.gestion_notes.model.Niveau;

@Repository
public interface ModuleRepository extends JpaRepository<Module, Long> {

	Optional<Module> findByTitre(String titre);
	Module findByCode(String code);
	List<Module> findByNiveauId(Long niveauId);
	
	  // Add this method to fetch modules by Niveau
    List<Module> findByNiveau(Niveau niveau);

}
