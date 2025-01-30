package com.jmp.gestion_notes.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jmp.gestion_notes.exception.ResourceNotFoundException;

import com.jmp.gestion_notes.model.Matiere;
import com.jmp.gestion_notes.model.Module;
import com.jmp.gestion_notes.model.Enseignant;

import com.jmp.gestion_notes.repo.MatiereRepository;
import com.jmp.gestion_notes.repo.ModuleRepository;
import com.jmp.gestion_notes.repo.EnseignantRepository;

@Service
public class MatiereService {
	private final MatiereRepository matiereRepository;
	private final ModuleRepository moduleRepository;
	private final EnseignantRepository enseignantRepository;

	@Autowired
	public MatiereService(MatiereRepository matiereRepository, ModuleRepository moduleRepository, EnseignantRepository enseignantRepository) {
		this.matiereRepository = matiereRepository;
		this.moduleRepository = moduleRepository;
		this.enseignantRepository = enseignantRepository;
	}
	
	// create matiere
	public Matiere createMatiere(Matiere matiere, Long id_module, Long id_enseignant) {
		
		Module module = moduleRepository.findById(id_module).orElseThrow(() -> new ResourceNotFoundException("module", "id", id_module));
		Enseignant enseignant = enseignantRepository.findById(id_enseignant).orElseThrow(() -> new ResourceNotFoundException("enseignant", "id", id_module));
		
		matiere.setModule(module);
		matiere.setEnseignant(enseignant);
		
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
	
    public List<Matiere> getMatieresByModuleId(Long moduleId) {
        return matiereRepository.findByModuleId(moduleId);
    }
}
