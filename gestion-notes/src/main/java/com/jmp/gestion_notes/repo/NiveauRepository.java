package com.jmp.gestion_notes.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.jmp.gestion_notes.model.Niveau;

public interface NiveauRepository extends JpaRepository<Niveau, Long> {
	Niveau findByAlias(String alias);
	
	@Query("SELECT n FROM Niveau n WHERE n.id = (SELECT n2.niveauSuivant.id FROM Niveau n2 WHERE n2.id = :niveauId)")
	Niveau findNiveauSuivant(@Param("niveauId") Long niveauId);

	
}
