package com.jmp.gestion_notes.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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
    
    @GetMapping("/download/delinotes")
    public ResponseEntity<ByteArrayResource> downloadDeliNotes(@RequestParam Long id_niveau) throws Exception {
    	byte[] excelBytes = excelService.generateDeliberationFile(id_niveau);
    	
        // Prepare response
        ByteArrayResource resource = new ByteArrayResource(excelBytes);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=collect-deliberation-notes-niveau.xlsx")
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(resource);
    }
    
    @PostMapping("/upload/collectnotes")
    public ResponseEntity<Map<String, String>> importCollectNotes(@RequestParam("file") MultipartFile file){
    	Map<String, String> response = new HashMap<>();
        try {
            
            String message = excelService.importer_collect_notes(file);
            response.put("message", message);
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            e.printStackTrace();
            response.put("message", "Échec du traitement du fichier");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    @PostMapping("/upload/delinotes")
    public ResponseEntity<Map<String, String>>importDeliNotes(@RequestParam("file") MultipartFile file){
    	Map<String, String> response = new HashMap<>();
    	
        try {
			String message = excelService.importer_deliberation_note(file);
			response.put("message", message);
			return ResponseEntity.ok(response);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			response.put("message", "Échec du traitement du fichier");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
    }
    
    @PostMapping("/upload/inscription")
    public ResponseEntity<String> importInscription(@RequestParam("file") MultipartFile file){
        // Validate file type
        if (!file.getContentType().equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")) {
            return ResponseEntity.badRequest().body("Invalid file type. Please upload an Excel file.");
        }
        
        // Process the file
        try {
			excelService.importer_inscription_reinscription(file);
			
			return ResponseEntity.ok("File uploaded and processed successfully!");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to process the file.");
		}
    }
    
    
    @PostMapping("/upload/sp")
    public ResponseEntity<Map<String, String>> importSp(@RequestParam("file") MultipartFile file){
    	Map<String, String> response = new HashMap<>();
    	
        try {
			String message = excelService.importer_sp(file);
			response.put("message", message);
			return ResponseEntity.ok(response);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			response.put("message", "Échec du traitement du fichier");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
    }

}
