package com.jmp.gestion_notes.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.jmp.gestion_notes.service.FiliereService;
import com.jmp.gestion_notes.model.Filiere;

@CrossOrigin(origins = { "http://localhost:3000", "http://localhost:8080", "http://localhost:8081" })
@RestController
@RequestMapping("/filieres")
public class FiliereController {
	
	@Autowired
	private FiliereService filierService;
	
	// endpoint to get all filieres
    @GetMapping("")
    public ResponseEntity<List<Filiere>> getAllUsers() {
        List<Filiere> filieres = filierService.getAllFilieres();
        return new ResponseEntity<>(filieres, HttpStatus.OK);
    }
    
    // endpoint to get a specific filiere by id
    @GetMapping("/{id}")
    public ResponseEntity<Filiere> getFiliereById(@PathVariable Long id){
    	Filiere filiere = filierService.getFiliereById(id);
    	return new ResponseEntity<>(filiere, HttpStatus.OK);
    }
    
    @PostMapping("")
    public ResponseEntity<Filiere> createFiliere(@RequestBody Filiere filiere, @RequestParam Long id_coordinateur){
    	Filiere Newfiliere = filierService.addFiliere(filiere, id_coordinateur);
    	return new ResponseEntity<>(Newfiliere, HttpStatus.CREATED);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Filiere> updateFiliere(@PathVariable Long id, @RequestBody Filiere filiere){
    	Filiere updatefiliere = filierService.updateFiliere(filiere, id);
    	return new ResponseEntity<>(updatefiliere, HttpStatus.OK);
    }
    
    // Endpoint to delete a user by id
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFiliere(@PathVariable Long id) {
    	filierService.deleteFiliere(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    
    
    
}
