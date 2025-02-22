package com.jmp.gestion_notes.controller;

import com.jmp.gestion_notes.model.Module;
import com.jmp.gestion_notes.model.Resultat;
import com.jmp.gestion_notes.service.ResultatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/resultats")
public class ResultatController {

    @Autowired
    private ResultatService resultatService;





    // Check if a student is eligible for a level
    @GetMapping("/eligibilite/{idEtudiant}/{idNiveau}")
    public ResponseEntity<Boolean> checkEligibilite(@PathVariable Long idEtudiant, @PathVariable Long idNiveau) {
        boolean isEligible = resultatService.estEligibleAuNiveau(idEtudiant, idNiveau);
        return new ResponseEntity<>(isEligible, HttpStatus.OK);
    }

    // Check if a student is ajourné (failed)
    @GetMapping("/ajourne/{idEtudiant}")
    public ResponseEntity<Boolean> checkAjourne(@PathVariable Long idEtudiant) {
        boolean isAjourne = resultatService.estEtudiantAjourne(idEtudiant);
        return new ResponseEntity<>(isAjourne, HttpStatus.OK);
    }


    // Verify previous enrollments for a student
    @GetMapping("/verifier-inscriptions/{idEtudiant}/{idNiveau}")
    public ResponseEntity<Boolean> verifierInscriptions(@PathVariable Long idEtudiant, @PathVariable Long idNiveau) {
        boolean isConsistent = resultatService.verifierInscriptionsPrecedentes(idEtudiant, idNiveau);
        return new ResponseEntity<>(isConsistent, HttpStatus.OK);
    }
    
    
    @GetMapping("/modules-non-valides/{idEtudiant}")
    public ResponseEntity<List<Module>> getModulesNonValides(@PathVariable Long idEtudiant) {
        List<Module> modules = resultatService.getModulesNonValides(idEtudiant);
        return new ResponseEntity<>(modules, HttpStatus.OK);
    }
    
    
    
    // Endpoint to check niveau consistency
    @GetMapping("/check-consistency/{idEtudiant}/{idNiveau}")
    public ResponseEntity<Boolean> checkNiveauConsistency(@PathVariable Long idEtudiant, @PathVariable Long idNiveau) {
        boolean isConsistent = resultatService.isNiveauConsistent(idEtudiant, idNiveau);
        return new ResponseEntity<>(isConsistent, HttpStatus.OK);
    }

    // Endpoint to get all results for a student
    @GetMapping("/etudiant/{idEtudiant}")
    public ResponseEntity<List<Resultat>> getResultatsByEtudiant(@PathVariable Long idEtudiant) {
        List<Resultat> resultats = resultatService.getResultatsByEtudiant(idEtudiant);
        return new ResponseEntity<>(resultats, HttpStatus.OK);
    }

    // Endpoint to add a new result
    @PostMapping("")
    public ResponseEntity<Resultat> addResultat(@RequestBody Resultat resultat) {
        Resultat newResultat = resultatService.addResultat(resultat);
        return new ResponseEntity<>(newResultat, HttpStatus.CREATED);
    }
}