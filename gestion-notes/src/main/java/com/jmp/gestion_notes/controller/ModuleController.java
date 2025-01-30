package com.jmp.gestion_notes.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jmp.gestion_notes.model.Module;
import com.jmp.gestion_notes.service.ModuleService;

@RestController
@RequestMapping("/modules")
public class ModuleController {
	@Autowired
	private ModuleService moduleService;
	
	// endpoint to get all filieres
    @GetMapping("")
    public ResponseEntity<List<Module>> getAllUsers() {
        List<Module> modules = moduleService.getAllModules();
        return new ResponseEntity<>(modules, HttpStatus.OK);
    }
    
    // endpoint to get a specific filiere by id
    @GetMapping("/{id}")
    public ResponseEntity<Module> getModuleById(@PathVariable Long id){
    	Module module = moduleService.getModuleById(id);
    	return new ResponseEntity<>(module, HttpStatus.OK);
    }
    
    // get by name
    @GetMapping("/partitre")
    public ResponseEntity<Module> getModuleByTitre(@RequestParam String titre){
    	Module module = moduleService.getModuleByTitre(titre);
    	return new ResponseEntity<>(module, HttpStatus.OK);
    }
    
    @PostMapping("")
    public ResponseEntity<Module> createModule(@RequestBody Module module, @RequestParam Long id_niveau, @RequestParam Long id_resp){
    	Module newModule = moduleService.createModule(module, id_niveau, id_resp);
    	return new ResponseEntity<>(newModule, HttpStatus.CREATED);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Module> updateModule(@PathVariable Long id, @RequestBody Module filiere){
    	Module updateModule = moduleService.updateModule(filiere, id);
    	return new ResponseEntity<>(updateModule, HttpStatus.OK);
    }
    
    // Endpoint to delete a module by id
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteModule(@PathVariable Long id) {
    	moduleService.deleteModule(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
