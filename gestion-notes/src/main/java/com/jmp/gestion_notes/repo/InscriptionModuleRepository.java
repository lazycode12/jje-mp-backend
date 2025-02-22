package com.jmp.gestion_notes.repo;



import com.jmp.gestion_notes.model.Module;
import com.jmp.gestion_notes.model.Etudiant;
import com.jmp.gestion_notes.model.InscriptionModule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InscriptionModuleRepository extends JpaRepository<InscriptionModule, Long> {
    @Query("SELECT im.module FROM InscriptionModule im WHERE im.etudiant.id = :etudiantId AND im.valide = false")
    List<Module> findModulesNonValides(@Param("etudiantId") Long etudiantId);

    boolean existsByEtudiantAndModule(Etudiant etudiant, Module module);
}

