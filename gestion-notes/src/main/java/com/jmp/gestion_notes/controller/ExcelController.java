package com.jmp.gestion_notes.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.core.io.ByteArrayResource;

import com.jmp.gestion_notes.service.ExcelService;

@RestController
@RequestMapping("/excel")
public class ExcelController {
	
	@Autowired
	private ExcelService excelService;
	
    @GetMapping("/download/collectnotes")
    public ResponseEntity<ByteArrayResource> downloadCollectNotes(@RequestParam Long id_semester, @RequestParam Long id_niveau, @RequestParam Long id_module) throws Exception {
        // Generate Excel file
        byte[] excelBytes = excelService.generateCollectNotesParModule(id_semester, id_niveau, id_module);

        // Prepare response
        ByteArrayResource resource = new ByteArrayResource(excelBytes);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=collect-notes-module.xlsx")
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(resource);
    }

}
