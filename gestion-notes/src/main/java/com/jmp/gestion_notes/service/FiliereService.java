package com.jmp.gestion_notes.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jmp.gestion_notes.model.Filiere;
import com.jmp.gestion_notes.model.Enseignant;
import com.jmp.gestion_notes.repo.FiliereRepository;
import com.jmp.gestion_notes.repo.EnseignantRepository;
import com.jmp.gestion_notes.exception.ResourceNotFoundException;

@Service
public class FiliereService {
	
	private final FiliereRepository filiereRepo;
	private final EnseignantRepository enseignantRepository;
	
	@Autowired
	public FiliereService(FiliereRepository filiereRepo, EnseignantRepository enseignantRepository) {
		this.filiereRepo = filiereRepo;
		this.enseignantRepository = enseignantRepository;
	}
	
	// create Filiere
	public Filiere addFiliere(Filiere filiere, Long id_coordinateur) {
		Enseignant coordinateur = enseignantRepository.findById(id_coordinateur).orElseThrow(() -> new ResourceNotFoundException("enseignant", "id", id_coordinateur));
		filiere.setCoordinateur(coordinateur);
		return filiereRepo.save(filiere);
	}
	
	public List<Filiere> getAllFilieres(){
		return filiereRepo.findAll();
	}
	
	public Filiere updateFiliere(Filiere updatefiliere, Long id) {
		Filiere filiere = getFiliereById(id);
		
		filiere.setAlias(updatefiliere.getAlias());
		filiere.setIntitule(updatefiliere.getIntitule());
		filiere.setAnne_fin_accreditation(updatefiliere.getAnne_fin_accreditation());
		filiere.setAnne_fin_accreditation(updatefiliere.getAnne_accreditation());
		filiere.setX(updatefiliere.getX());
		filiere.setY(updatefiliere.getY());
		
		return filiereRepo.save(filiere);
	}
	
	public Filiere getFiliereById(long id) {
		return filiereRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("filiere", "id", id));
	}
	
	public void deleteFiliere(Long id) {
		Filiere f = getFiliereById(id);
		filiereRepo.delete(f);
	}
	
}
