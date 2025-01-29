package com.jmp.gestion_notes.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jmp.gestion_notes.exception.ResourceNotFoundException;
import com.jmp.gestion_notes.model.Matiere;
import com.jmp.gestion_notes.model.Module;
import com.jmp.gestion_notes.repo.MatiereRepository;
import com.jmp.gestion_notes.repo.ModuleRepository;

@Service
public class MatiereService {
	private final MatiereRepository matiereRepository;
	private final ModuleRepository moduleRepository;

	@Autowired
	public MatiereService(MatiereRepository matiereRepository, ModuleRepository moduleRepository) {
		this.matiereRepository = matiereRepository;
		this.moduleRepository = moduleRepository;
	}
	
	// create matiere
	public Matiere createMatiere(Matiere matiere, Long id_module) {
		
		Module module = moduleRepository.findById(id_module).orElseThrow(() -> new ResourceNotFoundException("module", "id", id_module));
		
		matiere.setModule(module);
		
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
