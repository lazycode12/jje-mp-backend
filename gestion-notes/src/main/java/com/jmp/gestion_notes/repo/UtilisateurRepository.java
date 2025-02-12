package com.jmp.gestion_notes.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jmp.gestion_notes.model.Personne;
import com.jmp.gestion_notes.model.Utilisateur;

public interface UtilisateurRepository extends JpaRepository<Utilisateur, Long> {
	boolean existsByPersonne(Personne personne);
	Optional<Utilisateur> findByPersonne(Personne personne);
}
