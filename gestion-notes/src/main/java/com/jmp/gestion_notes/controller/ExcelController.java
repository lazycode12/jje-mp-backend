package com.jmp.gestion_notes.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
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
    
    @PostMapping("/upload/collectnotes")
    public ResponseEntity<String> importCollectNotes(@RequestParam("file") MultipartFile file){
        try {
            // Validate file type
            if (!file.getContentType().equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")) {
                return ResponseEntity.badRequest().body("Invalid file type. Please upload an Excel file.");
            }

            // Process the file
            excelService.importer_collect_notes(file);

            return ResponseEntity.ok("File uploaded and processed successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to process the file.");
        }
    }

}
