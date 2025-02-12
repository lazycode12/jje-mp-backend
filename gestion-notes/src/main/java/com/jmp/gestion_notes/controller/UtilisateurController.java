package com.jmp.gestion_notes.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.jmp.gestion_notes.model.Utilisateur;
import com.jmp.gestion_notes.service.UtilisateurService;

@RestController
@RequestMapping("/users")
public class UtilisateurController {

	@Autowired
	private UtilisateurService utilisateurService;
	
	// endpoint to get all filieres
    @GetMapping("")
    public ResponseEntity<List<Utilisateur>> getAllUsers() {
        List<Utilisateur> users = utilisateurService.getAllUtilisateurs();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }
    
    // endpoint to get a specific filiere by id
    @GetMapping("/{id}")
    public ResponseEntity<Utilisateur> getUtilisateurById(@PathVariable Long id){
    	Utilisateur user = utilisateurService.getUtilisateurById(id);
    	return new ResponseEntity<>(user, HttpStatus.OK);
    }
    // initialiser le mot de passe d'un utilisateur
    @GetMapping("/initpw/{id}")
    public String initializePassword(@PathVariable Long id) {
        return utilisateurService.initPass(id);
    }
    
    
    @PostMapping("")
    public Object createUtilisateur(@RequestBody Utilisateur user, @RequestParam Long id_personne){
    	return utilisateurService.createUtilisateur(user, id_personne);
//    	return new ResponseEntity<>(password, HttpStatus.CREATED);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Utilisateur> updateUtilisateur(@PathVariable Long id, @RequestBody Utilisateur filiere){
    	Utilisateur updatefiliere = utilisateurService.updateUtilisateur(filiere, id);
    	return new ResponseEntity<>(updatefiliere, HttpStatus.OK);
    }
    
    // Endpoint to delete a user by id
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUtilisateur(@PathVariable Long id) {
    	utilisateurService.deleteUtilisateur(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
