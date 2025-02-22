package com.jmp.gestion_notes.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.jmp.gestion_notes.model.Etudiant;
import com.jmp.gestion_notes.model.Niveau;

public interface EtudiantRepository extends JpaRepository<Etudiant, Long> {
	
	    
    @Query("SELECT e FROM Etudiant e WHERE LOWER(e.nom) LIKE LOWER(CONCAT('%', :nom, '%')) AND LOWER(e.prenom) LIKE LOWER(CONCAT('%', :prenom, '%'))")
    List<Etudiant> findByNomAndPrenom(@Param("nom") String nom, @Param("prenom") String prenom);


    Optional<Etudiant> findByCne(String cne);
    
    List<Etudiant> findByNiveauId(Long niveauId);

    List<Etudiant> findByCneOrNomOrPrenom(String cne, String nom, String prenom);

}
