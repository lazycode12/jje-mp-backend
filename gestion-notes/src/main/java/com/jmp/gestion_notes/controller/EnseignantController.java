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
import org.springframework.web.bind.annotation.RestController;

import com.jmp.gestion_notes.model.Enseignant;
import com.jmp.gestion_notes.service.EnseignantService;

@RestController
@RequestMapping("/enseignants")
public class EnseignantController {

	@Autowired
	private EnseignantService enseignantService;
	
	// endpoint to get all enseignants
    @GetMapping("")
    public ResponseEntity<List<Enseignant>> getAllUsers() {
        List<Enseignant> enseignants = enseignantService.getAllEnseignants();
        return new ResponseEntity<>(enseignants, HttpStatus.OK);
    }
    
    // endpoint to get a specific enseignant by id
    @GetMapping("/{id}")
    public ResponseEntity<Enseignant> getEnseignantById(@PathVariable Long id){
    	Enseignant enseignant = enseignantService.getEnseignantById(id);
    	return new ResponseEntity<>(enseignant, HttpStatus.OK);
    }
    
    @PostMapping("")
    public ResponseEntity<Enseignant> createEnseignant(@RequestBody Enseignant enseignant){
    	Enseignant Newenseignant = enseignantService.addEnseignant(enseignant);
    	return new ResponseEntity<>(Newenseignant, HttpStatus.CREATED);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Enseignant> updateEnseignant(@PathVariable Long id, @RequestBody Enseignant enseignant){
    	Enseignant updateenseignant = enseignantService.updateEnseignant(enseignant, id);
    	return new ResponseEntity<>(updateenseignant, HttpStatus.OK);
    }
    
    // Endpoint to delete a user by id
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEnseignant(@PathVariable Long id) {
    	enseignantService.deleteEnseignant(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
