package com.jmp.gestion_notes.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jmp.gestion_notes.model.Niveau;
import com.jmp.gestion_notes.model.Filiere;
import com.jmp.gestion_notes.repo.NiveauRepository;
import com.jmp.gestion_notes.repo.FiliereRepository;
import com.jmp.gestion_notes.exception.ResourceNotFoundException;

@Service
public class NiveauService {

	private final NiveauRepository niveauRepository;
	private final FiliereRepository filiereRepository;

	@Autowired
	public NiveauService(NiveauRepository niveauRepository, FiliereRepository filiereRepository) {
		this.niveauRepository = niveauRepository;
		this.filiereRepository = filiereRepository;
	}
	
	// create niveau
	public Niveau createNiveau(Niveau niveau, Long id_filiere, Long id_niveau_suivant) {
		
		Filiere filiere = filiereRepository.findById(id_filiere).orElseThrow(() -> new ResourceNotFoundException("filiere", "id", id_filiere));
		niveau.setFiliere(filiere);
		
		if(id_niveau_suivant != null) {
			Niveau n = niveauRepository.findById(id_niveau_suivant).orElseThrow(() -> new ResourceNotFoundException("Niveau", "id", id_filiere));
			niveau.setNiveauSuivant(n);
		}else {
			niveau.setNiveauSuivant(null);
		}
		
		return niveauRepository.save(niveau);
	}
	
	public List<Niveau> getAllNiveaux(){
		return niveauRepository.findAll();
	}
	
	public Niveau updateNiveau(Niveau updateNiveau, Long id) {
		Niveau niveau = getNiveaueById(id);
		niveau.setAlias(updateNiveau.getAlias());
		return niveauRepository.save(niveau);
	}
	
	public Niveau getNiveaueById(long id) {
		return niveauRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("niveau", "id", id));
	}
	
	public Niveau getNiveauByAlias(String alias) {
		return niveauRepository.findByAlias(alias);
	}
	
	public void deleteNiveau(Long id) {
		Niveau niveau = getNiveaueById(id);
		niveauRepository.delete(niveau);
	}
	
	
}
