package com.jmp.gestion_notes.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jmp.gestion_notes.model.Etudiant;

public interface EtudiantRepository extends JpaRepository<Etudiant, Long> {

}
