package com.jmp.gestion_notes.service;

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

import java.io.ByteArrayOutputStream;
import java.io.IOException;

//import com.jmp.gestion_notes.service.SemesterService;
//import com.jmp.gestion_notes.service.ModuleService;
//import com.jmp.gestion_notes.service.NiveauService;
//import com.jmp.gestion_notes.service.EtudiantService;

@Service
public class ExcelService {
	
	private SemesterService semesterService;
	private ModuleService moduleService;
	private NiveauService niveauService;
	private EtudiantService etudiantService;
	private MatiereService matiereService;
	
	@Autowired
	public ExcelService(SemesterService semesterService, ModuleService moduleService, NiveauService niveauService, EtudiantService etudiantService, MatiereService matiereService) throws IOException {
		this.semesterService = semesterService;
		this.moduleService = moduleService;
		this.niveauService = niveauService;
		this.etudiantService = etudiantService;
		this.matiereService = matiereService;
		
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
        headerRow1.createCell(5).setCellValue(semester.getAnne_etude());
        
        Row headerRow2 = sheet.createRow(1);
        headerRow2.createCell(0).setCellValue("Enseignant");
        headerRow2.createCell(1).setCellValue(module.getResp().getPrenom() + " " + module.getResp().getNom());
        
        headerRow2.createCell(3).setCellValue("Session");
        headerRow2.createCell(4).setCellValue(semester.getSession());
        
        headerRow2.createCell(5).setCellValue("Niveau");
        headerRow2.createCell(6).setCellValue(niveau.getAlias());
        
        //empty row
        Row emptryRow = sheet.createRow(2);
        
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
}
