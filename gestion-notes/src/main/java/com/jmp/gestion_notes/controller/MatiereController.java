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

import com.jmp.gestion_notes.model.Matiere;
import com.jmp.gestion_notes.service.MatiereService;

@RestController
@RequestMapping("/matieres")
public class MatiereController {
	
	@Autowired
	private MatiereService matiereService;
	
	// endpoint to get all matieres
    @GetMapping("")
    public ResponseEntity<List<Matiere>> getAllMatieres() {
        List<Matiere> matieres = matiereService.getAllMatieres();
        return new ResponseEntity<>(matieres, HttpStatus.OK);
    }
    
    // endpoint to get a specific matiere by id
    @GetMapping("/{id}")
    public ResponseEntity<Matiere> getMatiereById(@PathVariable Long id){
    	Matiere matiere = matiereService.getMatiereById(id);
    	return new ResponseEntity<>(matiere, HttpStatus.OK);
    }
    
    @PostMapping("")
    public ResponseEntity<Matiere> createMatiere(@RequestBody Matiere matiere, @RequestParam Long id_module, @RequestParam Long id_ensignant){
    	Matiere newMatiere = matiereService.createMatiere(matiere, id_module, id_ensignant);
    	return new ResponseEntity<>(newMatiere, HttpStatus.CREATED);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Matiere> updateMatiere(@PathVariable Long id, @RequestBody Matiere filiere){
    	Matiere updateMatiere = matiereService.updateMatiere(filiere, id);
    	return new ResponseEntity<>(updateMatiere, HttpStatus.OK);
    }
    
    // Endpoint to delete a matiere by id
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMatiere(@PathVariable Long id) {
    	matiereService.deleteMatiere(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    
    @GetMapping("/module/{moduleId}")
    public List<Matiere> getMatieresByModule(@PathVariable Long moduleId) {
        return matiereService.getMatieresByModuleId(moduleId);
    }
}
