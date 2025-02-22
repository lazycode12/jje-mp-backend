package com.jmp.gestion_notes.repo;

import com.jmp.gestion_notes.model.Resultat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResultatRepository extends JpaRepository<Resultat, Long> {
    List<Resultat> findByEtudiantId(Long idEtudiant);

}