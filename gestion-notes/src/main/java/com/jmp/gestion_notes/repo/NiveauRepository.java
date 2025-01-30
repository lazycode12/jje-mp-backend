package com.jmp.gestion_notes.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import com.jmp.gestion_notes.model.Niveau;

public interface NiveauRepository extends JpaRepository<Niveau, Long> {
	Niveau findByAlias(String alias);
}
