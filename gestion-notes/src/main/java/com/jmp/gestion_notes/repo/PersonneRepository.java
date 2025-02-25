package com.jmp.gestion_notes.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jmp.gestion_notes.model.Personne;

public interface PersonneRepository extends JpaRepository<Personne, Long> {
    // Find a person by their name
    Optional<Personne> findByNom(String nom);

    // Find a person by their email
    Optional<Personne> findByEmail(String email);

    // Find a person by their CIN
    Optional<Personne> findByCin(String cin);

    // Find all persons by their name
    List<Personne> findAllByNom(String nom);
}
