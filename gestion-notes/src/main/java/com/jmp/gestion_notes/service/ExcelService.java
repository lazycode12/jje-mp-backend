package com.jmp.gestion_notes.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jmp.gestion_notes.model.Module;
import com.jmp.gestion_notes.model.Semester;
import com.jmp.gestion_notes.model.Niveau;
import com.jmp.gestion_notes.model.Etudiant;
import com.jmp.gestion_notes.model.Matiere;
import com.jmp.gestion_notes.model.Note;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import java.io.InputStream;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ExcelService {
	
	private SemesterService semesterService;
	private ModuleService moduleService;
	private NiveauService niveauService;
	private EtudiantService etudiantService;
	private MatiereService matiereService;
	private NoteService noteService;
	
	@Autowired
	public ExcelService(SemesterService semesterService, ModuleService moduleService, NiveauService niveauService, EtudiantService etudiantService, MatiereService matiereService, NoteService noteService) throws IOException {
		this.semesterService = semesterService;
		this.moduleService = moduleService;
		this.niveauService = niveauService;
		this.etudiantService = etudiantService;
		this.matiereService = matiereService;
		this.noteService = noteService;
		
	}


	//  Générer Fichiers de collecte des notes par module
	public byte[] generateCollectNotesParModule(Long id_semester, Long id_niveau, Long id_module) {
		
		Semester semester = semesterService.getSemesterById(id_semester);
		Niveau niveau = niveauService.getNiveaueById(id_niveau);
		Module module = moduleService.getModuleById(id_module);
		
        // list etudiants par niveau
        List<Etudiant> etudiants = etudiantService.getEtudiantsByNiveau(id_niveau);
        
        //List matieres par module
        List<Matiere> matieres = matiereService.getMatieresByModuleId(id_module);
		
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("collect-notes-par-module");
        
        // Create header rows
        Row headerRow1 = sheet.createRow(0);
        headerRow1.createCell(0).setCellValue("Module");
        headerRow1.createCell(1).setCellValue(module.getTitre());
        
        headerRow1.createCell(2).setCellValue("Semester");
        headerRow1.createCell(3).setCellValue(semester.getSemester());
        
        headerRow1.createCell(4).setCellValue("Annee");
        headerRow1.createCell(5).setCellValue(semester.getanneEtude());
        
        Row headerRow2 = sheet.createRow(1);
        headerRow2.createCell(0).setCellValue("Enseignant");
        headerRow2.createCell(1).setCellValue(module.getResp().getPrenom() + " " + module.getResp().getNom());
        
        headerRow2.createCell(2).setCellValue("Session");
        headerRow2.createCell(3).setCellValue(semester.getSession());
        
        headerRow2.createCell(4).setCellValue("Niveau");
        headerRow2.createCell(5).setCellValue(niveau.getAlias());
        
        //empty row
        sheet.createRow(2);
        
        //main headers row
        Row headerRow3 = sheet.createRow(3);
        headerRow3.createCell(0).setCellValue("ID");
        headerRow3.createCell(1).setCellValue("CNE");
        headerRow3.createCell(2).setCellValue("NOM");
        headerRow3.createCell(3).setCellValue("PRENOM");
        
        int matiereIndex = 4;
        for(Matiere matiere: matieres) {
        	headerRow3.createCell(matiereIndex).setCellValue(matiere.getTitre());
        	matiereIndex++;
        }
        headerRow3.createCell(matiereIndex).setCellValue("Moyenne");
        headerRow3.createCell(matiereIndex+1).setCellValue("Validation");
        

        
        // Fill data rows
        int rowNum = 4;
        int cellIndex;
        for (Etudiant etudiant : etudiants) {
        	cellIndex=0;
            Row row = sheet.createRow(rowNum++);
            row.createCell(cellIndex++).setCellValue(etudiant.getId());
            row.createCell(cellIndex++).setCellValue(etudiant.getCne());
            row.createCell(cellIndex++).setCellValue(etudiant.getNom());
            row.createCell(cellIndex++).setCellValue(etudiant.getPrenom());
            for(int i = 0; i<matieres.size(); i++) {
            	row.createCell(cellIndex++);;
            	row.createCell(cellIndex++);
            }
            row.createCell(cellIndex++); //moyenne
            row.createCell(cellIndex++); //validation
        }
        
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
			workbook.write(outputStream);
			workbook.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        
        return outputStream.toByteArray();
        
        
	}
	
	
	
	
	
	
	//  importer Fichiers de collecte des notes par module
	 public void importer_collect_notes(MultipartFile file) throws IOException {
		 
	        try (InputStream inputStream = file.getInputStream()) {
	            Workbook workbook = new XSSFWorkbook(inputStream);
	            Sheet sheet = workbook.getSheetAt(0);
	            
	            String moduleTitre = sheet.getRow(0).getCell(1).getStringCellValue();
//	            String niveauAlias = sheet.getRow(1).getCell(4).getStringCellValue();
	            String semesterSemester = sheet.getRow(0).getCell(3).getStringCellValue();
	            String anne_etude = sheet.getRow(0).getCell(5).getStringCellValue();
	            String seesion = sheet.getRow(1).getCell(3).getStringCellValue();
	            
	            
//	            Niveau niveau = niveauService.getNiveauByAlias(niveauAlias);
	            Module module = moduleService.getModuleByTitre(moduleTitre);
	            Semester semester = semesterService.getSemesterByDetails(seesion, semesterSemester, anne_etude);
	            
	            List<Matiere> ModulemMtieres = matiereService.getMatieresByModuleId(module.getId());
	            List<Matiere> orderedMatieres = new ArrayList<>();
	            
	            
	            int matiereCellIndex = 4;
	            Matiere m;
	            Row titresRow = sheet.getRow(3);
                for(int i = 0; i < ModulemMtieres.size(); i++) {
                	m = matiereService.getMatiereByTitre(titresRow.getCell(matiereCellIndex++).getStringCellValue());
                	orderedMatieres.add(m);
                	
                }
	            
	            // Iterate through rows starting from the second row (skip header)
                int colIndex;
	            for (int i = 4; i <= sheet.getLastRowNum(); i++) {
	            	Note note = new Note();
	            	colIndex=4;
	                Row row = sheet.getRow(i);
	                if (row == null) continue;

	                for(Matiere matiere: orderedMatieres) {
	                	Etudiant etudiant = etudiantService.getEtudiantById((long) row.getCell(0).getNumericCellValue());
	                	float valeur = (float) row.getCell(colIndex++).getNumericCellValue();
	                	
	                	note.setValeur(valeur);
	                	noteService.addNote(note, etudiant.getId(), semester.getId(), matiere.getId());
	                }
	
	                }
	            workbook.close();
	            }
	 }
	
}
