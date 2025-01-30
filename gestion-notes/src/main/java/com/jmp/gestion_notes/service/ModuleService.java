package com.jmp.gestion_notes.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jmp.gestion_notes.exception.ResourceNotFoundException;
import com.jmp.gestion_notes.model.Module;
import com.jmp.gestion_notes.model.Niveau;
import com.jmp.gestion_notes.model.Enseignant;

import com.jmp.gestion_notes.repo.ModuleRepository;
import com.jmp.gestion_notes.repo.NiveauRepository;
import com.jmp.gestion_notes.repo.EnseignantRepository;

@Service
public class ModuleService {
	
	private final ModuleRepository moduleRepository;
	private final NiveauRepository niveauRepository;
	private final EnseignantRepository enseignantRepository;

	@Autowired
	public ModuleService(ModuleRepository moduleRepository, NiveauRepository niveauRepository, EnseignantRepository enseignantRepository) {
		this.moduleRepository = moduleRepository;
		this.niveauRepository = niveauRepository;
		this.enseignantRepository = enseignantRepository;
	}
	
	// create module
	public Module createModule(Module module, Long id_niveau, Long id_resp) {
		
		Niveau niveau = niveauRepository.findById(id_niveau).orElseThrow(() -> new ResourceNotFoundException("filiere", "id", id_niveau));
		Enseignant resp = enseignantRepository.findById(id_resp).orElseThrow(() -> new ResourceNotFoundException("enseignant", "id", id_niveau));
		
		module.setNiveau(niveau);
		module.setResp(resp);
		
		return moduleRepository.save(module);
	}
	
	public List<Module> getAllModules(){
		return moduleRepository.findAll();
	}
	
	public Module updateModule(Module updatemodule, Long id) {
		Module module = getModuleById(id);
		module.setCode(updatemodule.getCode());
		module.setTitre(updatemodule.getTitre());
		return moduleRepository.save(module);
	}
	
	public Module getModuleById(long id) {
		return moduleRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("module", "id", id));
	}
	
	public void deleteModule(Long id) {
		Module module = getModuleById(id);
		moduleRepository.delete(module);
	}
	
    public Module getModuleByTitre(String titre) {
        return moduleRepository.findByTitre(titre);
    }
}
