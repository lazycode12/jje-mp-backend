package com.jmp.gestion_notes.controller;

import com.jmp.gestion_notes.service.InscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/inscriptions")
public class InscriptionController {

    @Autowired
    private InscriptionService inscriptionService;

    @PostMapping("/import")
    public ResponseEntity<String> importInscriptions(@RequestParam("file") MultipartFile file) {
        try {
            inscriptionService.importerInscriptionsEtReinscriptions(file);
            return ResponseEntity.ok("Inscriptions and reinscriptions imported successfully.");
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Error processing the Excel file: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid file format: " + e.getMessage());
        }
    }

    @PostMapping("/ajourne/{idEtudiant}/inscrire-modules-suivants")
    public ResponseEntity<String> inscrireModulesNiveauSuivant(
            @PathVariable Long idEtudiant,
            @RequestBody List<Long> idModules) {
        try {
            inscriptionService.inscrireModulesNiveauSuivant(idEtudiant, idModules);
            return ResponseEntity.ok("Modules du niveau suivant inscrits avec succès !");
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erreur interne : " + e.getMessage());
        }
    }

    @GetMapping("/export")
    public ResponseEntity<byte[]> exporterEtudiants() {
        byte[] excelFile = inscriptionService.exporterListeEtudiants();
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=etudiants.xlsx")
                .body(excelFile);
    }
}