package com.jmp.gestion_notes.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jmp.gestion_notes.model.Niveau;
import com.jmp.gestion_notes.repo.NiveauRepository;
import com.jmp.gestion_notes.exception.ResourceNotFoundException;

@Service
public class NiveauService {

	private final NiveauRepository niveauRepository;

	@Autowired
	public NiveauService(NiveauRepository niveauRepository) {
		this.niveauRepository = niveauRepository;
	}
	
	// create niveau
	public Niveau createNiveau(Niveau niveau) {
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
	
	public void deleteNiveau(Long id) {
		Niveau niveau = getNiveaueById(id);
		niveauRepository.delete(niveau);
	}
	
	
}
