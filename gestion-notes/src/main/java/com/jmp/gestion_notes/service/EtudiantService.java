package com.jmp.gestion_notes.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jmp.gestion_notes.exception.ResourceNotFoundException;
import com.jmp.gestion_notes.model.Etudiant;
import com.jmp.gestion_notes.repo.EtudiantRepository;

@Service
public class EtudiantService {
	
	private final EtudiantRepository etudiantRepo;
	
	@Autowired
	public EtudiantService(EtudiantRepository etudiantRepo) {
		this.etudiantRepo = etudiantRepo;
	}
	
	// create Etudiant
	public Etudiant addEtudiant(Etudiant etudiant) {
		return etudiantRepo.save(etudiant);
	}
	
	public List<Etudiant> getAllEtudiants(){
		return etudiantRepo.findAll();
	}
	
	public Etudiant updateEtudiant(Etudiant updateetudiant, Long id) {
		Etudiant etudiant = getEtudiantById(id);
		
		etudiant.setNom(updateetudiant.getNom());
		etudiant.setPrenom(updateetudiant.getPrenom());
		etudiant.setCne(updateetudiant.getCne());
		
		return etudiantRepo.save(etudiant);
	}
	
	public Etudiant getEtudiantById(long id) {
		return etudiantRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("etudiant", "id", id));
	}
	
	public void deleteEtudiant(Long id) {
		Etudiant f = getEtudiantById(id);
		etudiantRepo.delete(f);
	}
}
