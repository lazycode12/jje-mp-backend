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

import com.jmp.gestion_notes.model.Etudiant;
import com.jmp.gestion_notes.service.EtudiantService;

@RestController
@RequestMapping("/etudiants")
public class EtudiantController {
	@Autowired
	private EtudiantService etudiantService;
	
	// endpoint to get all etudiants
    @GetMapping("")
    public ResponseEntity<List<Etudiant>> getAllEtudiants() {
        List<Etudiant> etudiants = etudiantService.getAllEtudiants();
        return new ResponseEntity<>(etudiants, HttpStatus.OK);
    }
    
    // endpoint to get a specific etudiant by id
    @GetMapping("/{id}")
    public ResponseEntity<Etudiant> getEtudiantById(@PathVariable Long id){
    	Etudiant etudiant = etudiantService.getEtudiantById(id);
    	return new ResponseEntity<>(etudiant, HttpStatus.OK);
    }
    
    // endpoint to get a specific etudiant by cne
    @GetMapping("/bycne")
    public ResponseEntity<Etudiant> getEtudiantByCne(@RequestParam String cne){
    	Etudiant etudiant = etudiantService.getEtudiantByCne(cne);
    	return new ResponseEntity<>(etudiant, HttpStatus.OK);
    }
    
    
    @PostMapping("")
    public ResponseEntity<Etudiant> createEtudiant(@RequestBody Etudiant etudiant, @RequestParam Long id_niveau){
    	Etudiant Newetudiant = etudiantService.addEtudiant(etudiant, id_niveau);
    	return new ResponseEntity<>(Newetudiant, HttpStatus.CREATED);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Etudiant> updateEtudiant(@PathVariable Long id, @RequestBody Etudiant etudiant){
    	Etudiant updateetudiant = etudiantService.updateEtudiant(etudiant, id);
    	return new ResponseEntity<>(updateetudiant, HttpStatus.OK);
    }
    
    // Endpoint to delete a user by id
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEtudiant(@PathVariable Long id) {
    	etudiantService.deleteEtudiant(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    
    @GetMapping("/niveau/{niveauId}")
    public List<Etudiant> getEtudiantsByNiveau(@PathVariable Long niveauId) {
        return etudiantService.getEtudiantsByNiveau(niveauId);
    }
}
