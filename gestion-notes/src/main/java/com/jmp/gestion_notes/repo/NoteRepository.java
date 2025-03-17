package com.jmp.gestion_notes.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jmp.gestion_notes.model.Etudiant;
import com.jmp.gestion_notes.model.Note;
import com.jmp.gestion_notes.model.Module;
import com.jmp.gestion_notes.model.Matiere;

public interface NoteRepository extends JpaRepository<Note, Long> {
	List<Note> findByEtudiantAndMatiere(Etudiant etudiant, Matiere matiere );
}
