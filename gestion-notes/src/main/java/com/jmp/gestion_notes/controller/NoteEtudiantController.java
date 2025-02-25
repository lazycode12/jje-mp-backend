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

import com.jmp.gestion_notes.model.NoteEtudiant;
import com.jmp.gestion_notes.service.NoteEtudiantService;

@RestController
@RequestMapping("/notesetudiant")
public class NoteEtudiantController {
	
	@Autowired
	private NoteEtudiantService noteEtudiantService;
	
	// endpoint to get all nots
    @GetMapping("")
    public ResponseEntity<List<NoteEtudiant>> getAllNoteEtudiant() {
        List<NoteEtudiant> nots = noteEtudiantService.getAllNoteEtudiants();
        return new ResponseEntity<>(nots, HttpStatus.OK);
    }
    
    // endpoint to get a specific not by id
    @GetMapping("/{id}")
    public ResponseEntity<NoteEtudiant> getNoteEtudiantById(@PathVariable Long id){
    	NoteEtudiant not = noteEtudiantService.getNoteEtudiantById(id);
    	return new ResponseEntity<>(not, HttpStatus.OK);
    }
    
    @GetMapping("/singlenote")
    public ResponseEntity<NoteEtudiant> getNoteEtudiantById(@RequestParam String cne, @RequestParam Long id_module, @RequestParam String anneEtude){
    	NoteEtudiant not = noteEtudiantService.getSingleNote(cne, id_module, anneEtude);
    	return new ResponseEntity<>(not, HttpStatus.OK);
    }
    
    @PostMapping("")
    public ResponseEntity<NoteEtudiant> createNoteEtudiant(@RequestBody NoteEtudiant not, @RequestParam Long id_etudiant, @RequestParam Long id_module){
    	NoteEtudiant Newnot = noteEtudiantService.addNoteEtudiant(not, id_etudiant,id_module);
    	return new ResponseEntity<>(Newnot, HttpStatus.CREATED);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<NoteEtudiant> updateNoteEtudiant(@PathVariable Long id, @RequestBody NoteEtudiant note){
    	NoteEtudiant updateNoteEtudiant = noteEtudiantService.updateNoteEtudiant(note, id);
    	return new ResponseEntity<>(updateNoteEtudiant, HttpStatus.OK);
    }
    
    // Endpoint to delete a user by id
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNoteEtudiant(@PathVariable Long id) {
    	noteEtudiantService.deleteNoteEtudiant(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
