package com.jmp.gestion_notes.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.jmp.gestion_notes.exception.ResourceNotFoundException;
import com.jmp.gestion_notes.model.Matiere;
import com.jmp.gestion_notes.repo.MatiereRepository;

public class MatiereService {
	private final MatiereRepository matiereRepository;

	@Autowired
	public MatiereService(MatiereRepository MatiereRepository) {
		this.matiereRepository = MatiereRepository;
	}
	
	// create matiere
	public Matiere createMatiere(Matiere matiere) {
		return matiereRepository.save(matiere);
	}
	
	public List<Matiere> getAllMatieres(){
		return matiereRepository.findAll();
	}
	
	public Matiere updateMatiere(Matiere updateMatiere, Long id) {
		Matiere Matiere = getMatiereById(id);
		Matiere.setTitre(updateMatiere.getTitre());
		return matiereRepository.save(Matiere);
	}
	
	public Matiere getMatiereById(long id) {
		return matiereRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Matiere", "id", id));
	}
	
	public void deleteMatiere(Long id) {
		Matiere matiere = getMatiereById(id);
		matiereRepository.delete(matiere);
	}
}
