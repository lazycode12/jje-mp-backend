package com.jmp.gestion_notes.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jmp.gestion_notes.exception.ResourceNotFoundException;
import com.jmp.gestion_notes.model.Etudiant;
import com.jmp.gestion_notes.model.Niveau;
import com.jmp.gestion_notes.repo.EtudiantRepository;
import com.jmp.gestion_notes.repo.NiveauRepository;

@Service
public class EtudiantService {
	
	private final EtudiantRepository etudiantRepo;
	private final NiveauRepository niveauRepository;
	
	@Autowired
	public EtudiantService(EtudiantRepository etudiantRepo, NiveauRepository niveauRepository) {
		this.etudiantRepo = etudiantRepo;
		this.niveauRepository = niveauRepository;
	}
	
	// create Etudiant
	public Etudiant addEtudiant(Etudiant etudiant, Long id_niveau) {
		Niveau niveau = niveauRepository.findById(id_niveau).orElseThrow(() -> new ResourceNotFoundException("Niveau", "id", id_niveau));
		etudiant.setNiveau(niveau);
		return etudiantRepo.save(etudiant);
	}
	
	public List<Etudiant> getAllEtudiants(){
		return etudiantRepo.findAll();
	}
	
	public Etudiant getEtudiantByCne(String cne) {
		return etudiantRepo.findByCne(cne);
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
	
    public List<Etudiant> getEtudiantsByNiveau(Long niveauId) {
        return etudiantRepo.findByNiveauId(niveauId);
    }
}
