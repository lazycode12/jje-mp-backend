package com.jmp.gestion_notes.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.jmp.gestion_notes.exception.ResourceNotFoundException;
import com.jmp.gestion_notes.model.Module;
import com.jmp.gestion_notes.repo.ModuleRepository;

public class ModuleService {
	private final ModuleRepository moduleRepository;

	@Autowired
	public ModuleService(ModuleRepository moduleRepository) {
		this.moduleRepository = moduleRepository;
	}
	
	// create module
	public Module createModule(Module module) {
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
}
