package com.jmp.gestion_notes.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jmp.gestion_notes.model.Personne;

public interface PersonneRepository extends JpaRepository<Personne, Long> {

}
