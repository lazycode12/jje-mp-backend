package com.jmp.gestion_notes.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jmp.gestion_notes.exception.ResourceNotFoundException;
import com.jmp.gestion_notes.model.Enseignant;
import com.jmp.gestion_notes.repo.EnseignantRepository;

@Service
public class EnseignantService {
	
	private final EnseignantRepository enseignantRepository;
	
	@Autowired
	public EnseignantService(EnseignantRepository enseignantRepository) {
		this.enseignantRepository = enseignantRepository;
	}
	
	// create Enseignant
	public Enseignant addEnseignant(Enseignant etudiant) {
		return enseignantRepository.save(etudiant);
	}
	
	public List<Enseignant> getAllEnseignants(){
		return enseignantRepository.findAll();
	}
	
	public Enseignant updateEnseignant(Enseignant updateEnseignant, Long id) {
		Enseignant enseignant = getEnseignantById(id);
		
		enseignant.setNom(updateEnseignant.getNom());
		enseignant.setPrenom(updateEnseignant.getPrenom());
		
		return enseignantRepository.save(enseignant);
	}
	
	public Enseignant getEnseignantById(long id) {
		return enseignantRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("etudiant", "id", id));
	}
	
	public Enseignant getEnseignantByNomAndPrenom(String nom, String prenom) {
		return enseignantRepository.findByNomAndPrenom(nom,prenom);
	}
	
	public void deleteEnseignant(Long id) {
		Enseignant e = getEnseignantById(id);
		enseignantRepository.delete(e);
	}
	
}
