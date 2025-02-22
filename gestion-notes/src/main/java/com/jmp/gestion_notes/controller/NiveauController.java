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

import com.jmp.gestion_notes.model.Niveau;
import com.jmp.gestion_notes.service.NiveauService;

@RestController
@RequestMapping("/niveaux")
public class NiveauController {
	
	@Autowired
	private NiveauService niveauService;
	
	// endpoint to get all Niveaus
    @GetMapping("")
    public ResponseEntity<List<Niveau>> getAllUsers() {
        List<Niveau> Niveaux = niveauService.getAllNiveaux();
        return new ResponseEntity<>(Niveaux, HttpStatus.OK);
    }
    
    // endpoint to get a specific Niveau by id
    @GetMapping("/{id}")
    public ResponseEntity<Niveau> getNiveauById(@PathVariable Long id){
    	Niveau Niveau = niveauService.getNiveauById(id);
    	return new ResponseEntity<>(Niveau, HttpStatus.OK);
    }
    
    @PostMapping("")
    public ResponseEntity<Niveau> createNiveau(@RequestBody Niveau Niveau, @RequestParam  Long filiere_id, @RequestParam(required = false) Long id_niveau_suivant){
    	Niveau NewNiveau = niveauService.createNiveau(Niveau, filiere_id, id_niveau_suivant);
    	return new ResponseEntity<>(NewNiveau, HttpStatus.CREATED);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Niveau> updateNiveau(@PathVariable Long id, @RequestBody Niveau Niveau, @RequestParam Long id_niveau_suivant){
    	Niveau updateNiveau = niveauService.updateNiveau(Niveau, id, id_niveau_suivant);
    	return new ResponseEntity<>(updateNiveau, HttpStatus.OK);
    }
    
    // Endpoint to delete a user by id
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNiveau(@PathVariable Long id) {
    	niveauService.deleteNiveau(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
