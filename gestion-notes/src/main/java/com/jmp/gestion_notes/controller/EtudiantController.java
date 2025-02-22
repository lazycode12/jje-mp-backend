package com.jmp.gestion_notes.controller;

import com.jmp.gestion_notes.model.CriteresRecherche;
import com.jmp.gestion_notes.model.Etudiant;
import com.jmp.gestion_notes.service.EtudiantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/etudiants")
public class EtudiantController {

    @Autowired
    private EtudiantService etudiantService;

    @GetMapping("")
    public ResponseEntity<List<Etudiant>> getAllEtudiants() {
        List<Etudiant> etudiants = etudiantService.getAllEtudiants();
        return new ResponseEntity<>(etudiants, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Etudiant> getEtudiantById(@PathVariable Long id) {
        Etudiant etudiant = etudiantService.getEtudiantById(id);
        return new ResponseEntity<>(etudiant, HttpStatus.OK);
    }

    @GetMapping("/bycne")
    public ResponseEntity<Etudiant> getEtudiantByCne(@RequestParam String cne) {
        Optional<Etudiant> etudiantOptional = etudiantService.getEtudiantByCne(cne);
        return etudiantOptional.map(etudiant -> new ResponseEntity<>(etudiant, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("")
    public ResponseEntity<Etudiant> createEtudiant(@RequestBody Etudiant etudiant, @RequestParam Long idNiveau) {
        Etudiant newEtudiant = etudiantService.addEtudiant(etudiant, idNiveau);
        return new ResponseEntity<>(newEtudiant, HttpStatus.CREATED);
    }

    
    
    
    @PutMapping("/{id}")
    public ResponseEntity<Etudiant> updateEtudiant(@PathVariable Long id, @RequestBody Etudiant etudiant) {
        Etudiant updatedEtudiant = etudiantService.updateEtudiant(etudiant, id);
        return new ResponseEntity<>(updatedEtudiant, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEtudiant(@PathVariable Long id) {
        etudiantService.deleteEtudiant(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/niveau/{niveauId}")
    public ResponseEntity<List<Etudiant>> getEtudiantsByNiveau(@PathVariable Long niveauId) {
        List<Etudiant> etudiants = etudiantService.getEtudiantsByNiveau(niveauId);
        return new ResponseEntity<>(etudiants, HttpStatus.OK);
    }

    @GetMapping("/rechercher")
    public ResponseEntity<List<Etudiant>> rechercherEtudiants(
            @RequestParam(required = false) String cne,
            @RequestParam(required = false) String nom,
            @RequestParam(required = false) String prenom) {
        CriteresRecherche criteres = new CriteresRecherche();
        criteres.setCne(cne);
        criteres.setNom(nom);
        criteres.setPrenom(prenom);
        List<Etudiant> etudiants = etudiantService.searchEtudiants(criteres);
        return new ResponseEntity<>(etudiants, HttpStatus.OK);
    }
}