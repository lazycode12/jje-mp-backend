package com.jmp.gestion_notes.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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

import com.jmp.gestion_notes.model.Personne;
import com.jmp.gestion_notes.model.Utilisateur;
import com.jmp.gestion_notes.service.PersonneService;
import com.jmp.gestion_notes.repo.UtilisateurRepository;

@RestController
@RequestMapping("/personnes")
public class PersonneController {
	@Autowired
	private PersonneService personneService;
	
	 @Autowired
	 private UtilisateurRepository utilisateurRepository;
	
	// endpoint to get all personnes
    @GetMapping("")
    public ResponseEntity<List<Personne>> getAllPersonnes() {
        List<Personne> personnes = personneService.getAllPersonnes();
        return new ResponseEntity<>(personnes, HttpStatus.OK);
    }
    
    // endpoint to get a specific personne by id
    @GetMapping("/{id}")
    public ResponseEntity<Personne> getPersonneById(@PathVariable Long id){
    	Personne personne = personneService.getPersonneById(id);
    	return new ResponseEntity<>(personne, HttpStatus.OK);
    }
    
    @GetMapping("/hasAccount")
    public ResponseEntity<Map<String, Object>> hasAccount(@RequestParam Long id) {
        Personne personne = personneService.getPersonneById(id);

        Map<String, Object> response = new HashMap<>();
        
        boolean hasAccount = personneService.hasAccount(personne);
        Optional<Utilisateur> user = utilisateurRepository.findByPersonne(personne);
        if(hasAccount) {
        	response.put("role", user.get().getRole());
        }
        
        response.put("hasAccount", hasAccount);

        return ResponseEntity.ok(response);
    }
    
    @PostMapping("")
    public ResponseEntity<Personne> createPersonne(@RequestBody Personne personne){
    	Personne newPersonne = personneService.createPersonne(personne);
    	return new ResponseEntity<>(newPersonne, HttpStatus.CREATED);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Personne> updatePersonne(@PathVariable Long id, @RequestBody Personne personne){
    	Personne updatePersonne = personneService.updatePersonne(personne, id);
    	return new ResponseEntity<>(updatePersonne, HttpStatus.OK);
    }
    
    // Endpoint to delete a personne by id
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePersonne(@PathVariable Long id) {
    	personneService.deletePersonne(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
