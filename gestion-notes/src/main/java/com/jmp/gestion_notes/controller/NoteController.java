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

import com.jmp.gestion_notes.model.Note;
import com.jmp.gestion_notes.service.NoteService;

@RestController
@RequestMapping("/notes")
public class NoteController {
	@Autowired
	private NoteService noteService;
	
	// endpoint to get all nots
    @GetMapping("")
    public ResponseEntity<List<Note>> getAllUsers() {
        List<Note> nots = noteService.getAllNotes();
        return new ResponseEntity<>(nots, HttpStatus.OK);
    }
    
    // endpoint to get a specific not by id
    @GetMapping("/{id}")
    public ResponseEntity<Note> getNoteById(@PathVariable Long id){
    	Note not = noteService.getNoteById(id);
    	return new ResponseEntity<>(not, HttpStatus.OK);
    }
    
    @PostMapping("")
    public ResponseEntity<Note> createNote(@RequestBody Note not, @RequestParam Long id_etudiant, @RequestParam Long id_semester, @RequestParam Long id_matiere){
    	Note Newnot = noteService.addNote(not, id_etudiant,id_semester, id_matiere );
    	return new ResponseEntity<>(Newnot, HttpStatus.CREATED);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Note> updateNote(@PathVariable Long id, @RequestBody Note note){
    	Note updateNote = noteService.updateNote(note, id);
    	return new ResponseEntity<>(updateNote, HttpStatus.OK);
    }
    
    // Endpoint to delete a user by id
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNote(@PathVariable Long id) {
    	noteService.deleteNote(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
