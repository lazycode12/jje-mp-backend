package com.jmp.gestion_notes.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jmp.gestion_notes.model.Enseignant;

public interface EnseignantRepository extends JpaRepository<Enseignant, Long> {
	Enseignant findByNomAndPrenom(String nom, String prenom);

}
