package com.jmp.gestion_notes.controller;

import com.jmp.gestion_notes.model.SessionType;
import com.jmp.gestion_notes.service.ExcelGenerationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/excel")
public class ExcelGenerationController {

    @Autowired
    private ExcelGenerationService excelGenerationService;

    @GetMapping("/generateModuleGrades/{moduleId}")
    public ResponseEntity<byte[]> generateModuleGradesFile(@PathVariable Long moduleId, @RequestParam String session) throws IOException {
        byte[] excelFile = excelGenerationService.generateModuleGradesFile(moduleId, SessionType.valueOf(session));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "module_grades.xlsx");

        return ResponseEntity.ok().headers(headers).body(excelFile);
    }

    @GetMapping("/deliberation/{niveauId}")
    public ResponseEntity<byte[]> generateDeliberationFile(@PathVariable Long niveauId) throws IOException {
        byte[] excelFile = excelGenerationService.generateDeliberationFile(niveauId);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "deliberation.xlsx");

        return ResponseEntity.ok().headers(headers).body(excelFile);
    }
    

    @PostMapping("/importGrades/{moduleId}")
    public ResponseEntity<String> importGrades(
            @PathVariable Long moduleId,
            @RequestParam SessionType session,
            @RequestParam("file") MultipartFile file) {
        try {
            excelGenerationService.importModuleGrades(file, moduleId, session);
            return ResponseEntity.ok("Grades imported successfully");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to import grades: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    
    
    @PostMapping("/importMatiereModuleEnseignant")
    public ResponseEntity<String> importMatiereModuleEnseignant(@RequestParam("file") MultipartFile file) {
        try {
            excelGenerationService.importMatiereModuleEnseignant(file);
            return ResponseEntity.ok("Matiere, Module, and Enseignant data imported successfully");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to import data: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    
}