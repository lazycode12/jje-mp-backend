package com.jmp.gestion_notes.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jmp.gestion_notes.model.Etudiant;
import com.jmp.gestion_notes.model.NoteEtudiant;
import com.jmp.gestion_notes.model.Module;


public interface NoteEtudiantRepository extends JpaRepository<NoteEtudiant, Long> {
	NoteEtudiant findByEtudiantIdAndModuleIdAndAnneEtude(Long id_etudiant, Long id_module, String anneEtude);
    NoteEtudiant findByEtudiantAndModule(Etudiant etudiant, Module module);

}
