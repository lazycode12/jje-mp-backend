package com.jmp.gestion_notes.controller;

import com.jmp.gestion_notes.model.SessionType;
import com.jmp.gestion_notes.service.ExcelGenerationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/excel")
public class ExcelGenerationController {

    @Autowired
    private ExcelGenerationService excelGenerationService;

    @GetMapping("/module/{moduleId}")
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
}