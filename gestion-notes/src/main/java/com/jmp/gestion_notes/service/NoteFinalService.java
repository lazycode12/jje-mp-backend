package com.jmp.gestion_notes.service;

import org.springframework.stereotype.Service;

import com.jmp.gestion_notes.exception.ResourceNotFoundException;
import com.jmp.gestion_notes.model.Etudiant;
import com.jmp.gestion_notes.model.Matiere;
import com.jmp.gestion_notes.model.Note;
import com.jmp.gestion_notes.model.NoteFinal;
import com.jmp.gestion_notes.model.Module;

import com.jmp.gestion_notes.repo.NoteFinalRepo;

@Service
public class NoteFinalService {
	
	private final NoteFinalRepo noteFinalRepo;
	
	
	public NoteFinalService(NoteFinalRepo noteFinalRepo) {
		super();
		this.noteFinalRepo = noteFinalRepo;
	}




	// create Note
	public NoteFinal addNote(NoteFinal nf,Etudiant etudiant, Module module) {
		nf.setEtudiant(etudiant);
		nf.setModule(module);
		
		return noteFinalRepo.save(nf);
	}
	
}
