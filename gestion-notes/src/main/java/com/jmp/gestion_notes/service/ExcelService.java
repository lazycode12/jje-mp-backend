package com.jmp.gestion_notes.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jmp.gestion_notes.dto.ImportResult;
import com.jmp.gestion_notes.model.*;
import com.jmp.gestion_notes.model.Module;

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
	private NoteEtudiantService noteEtudiantService;
	private EnseignantService enseignantService;
	private FiliereService filiereService;
	private NoteFinalService noteFinalService;
	
	@Autowired
	public ExcelService(SemesterService semesterService, ModuleService moduleService, NiveauService niveauService, EtudiantService etudiantService, MatiereService matiereService, NoteService noteService, NoteEtudiantService noteEtudiantService, EnseignantService enseignantService, FiliereService filiereService, NoteFinalService noteFinalService) throws IOException {
		this.semesterService = semesterService;
		this.moduleService = moduleService;
		this.niveauService = niveauService;
		this.etudiantService = etudiantService;
		this.matiereService = matiereService;
		this.noteService = noteService;
		this.noteEtudiantService = noteEtudiantService;
		this.enseignantService = enseignantService;
		this.filiereService = filiereService;
		this.noteFinalService = noteFinalService;
	}


	public byte[] generateCollectNotesParModule(Long id_semester, Long id_niveau, Long id_module) {
	    
	    Semester semester = semesterService.getSemesterById(id_semester);
	    Niveau niveau = niveauService.getNiveauById(id_niveau);
	    Module module = moduleService.getModuleById(id_module);
	    
	    // List of students by level
	    List<Etudiant> etudiants = etudiantService.getEtudiantsByNiveau(id_niveau);
	    
	    // List of subjects by module
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
	    headerRow2.createCell(1).setCellValue(module.getResponsable().getPrenom() + " " + module.getResponsable().getNom());
	    
	    headerRow2.createCell(2).setCellValue("Session");
	    headerRow2.createCell(3).setCellValue(semester.getSession());
	    
	    headerRow2.createCell(4).setCellValue("Niveau");
	    headerRow2.createCell(5).setCellValue(niveau.getAlias());
	    
	    // Empty row
	    sheet.createRow(2);
	    
	    // Main headers row
	    Row headerRow3 = sheet.createRow(3);
	    headerRow3.createCell(0).setCellValue("ID");
	    headerRow3.createCell(1).setCellValue("CNE");
	    headerRow3.createCell(2).setCellValue("NOM");
	    headerRow3.createCell(3).setCellValue("PRENOM");
	    
	    int subjectStartIndex = 4;
	    for (Matiere matiere : matieres) {
	        headerRow3.createCell(subjectStartIndex).setCellValue(matiere.getTitre());
	        subjectStartIndex++;
	    }
	    
	    headerRow3.createCell(subjectStartIndex).setCellValue("Moyenne");
	    headerRow3.createCell(subjectStartIndex + 1).setCellValue("Validation");
	    
	    // Fill student data
	    int rowNum = 4;
	    for (Etudiant etudiant : etudiants) {
	        int cellIndex = 0;
	        Row row = sheet.createRow(rowNum);
	        
	        row.createCell(cellIndex++).setCellValue(etudiant.getId());
	        row.createCell(cellIndex++).setCellValue(etudiant.getCne());
	        row.createCell(cellIndex++).setCellValue(etudiant.getNom());
	        row.createCell(cellIndex++).setCellValue(etudiant.getPrenom());
	        
	        // Leave subject cells empty for now (the teacher will fill them later)
	        int subjectEndIndex = cellIndex + matieres.size() - 1;
	        
	        for (int i = cellIndex; i <= subjectEndIndex; i++) {
	            row.createCell(i);
	        }
	        
	        // Set Excel formula for the average column
	        String startCol = CellReference.convertNumToColString(cellIndex);
	        String endCol = CellReference.convertNumToColString(subjectEndIndex);
	        String formula = "AVERAGE(" + startCol + (rowNum + 1) + ":" + endCol + (rowNum + 1) + ")";
	        
	        Cell avgCell = row.createCell(subjectEndIndex + 1);
	        avgCell.setCellFormula(formula);
	        
	        // Set Excel formula for the validation column
	        String validationFormula = "IF(" + avgCell.getAddress().formatAsString() + ">=10, \"V\", \"NV\")";
	        Cell validationCell = row.createCell(subjectEndIndex + 2);
	        validationCell.setCellFormula(validationFormula);
	        
	        rowNum++;
	    }
	    
	    // Convert workbook to byte array
	    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
	    try {
	        workbook.write(outputStream);
	        workbook.close();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	    
	    return outputStream.toByteArray();
	}
	
	
	//  importer Fichiers de collecte des notes par module
	 public String importer_collect_notes(MultipartFile file) throws IOException {
		 
		 if(!file.getContentType().equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")) {
         	String message = "Type de fichier non valide. Veuillez télécharger un fichier Excel.";
         	return message;
		 }
		 
	        try (InputStream inputStream = file.getInputStream()) {
	            Workbook workbook = new XSSFWorkbook(inputStream);
	            Sheet sheet = workbook.getSheetAt(0);
	            
	            if(sheet.getRow(0) == null || sheet.getRow(1) == null) {
	            	workbook.close();
	            	String message = "la structure n'est pas correcte";
	            	return message;
	            }
	            
	            
	            String moduleTitre = sheet.getRow(0).getCell(1).getStringCellValue();
	            String semesterSemester = sheet.getRow(0).getCell(3).getStringCellValue();
	            String anne_etude = sheet.getRow(0).getCell(5).getStringCellValue();
	            String seesion = sheet.getRow(1).getCell(3).getStringCellValue();
	            
	            
	            Module module = moduleService.getModuleByTitre(moduleTitre);
	            Semester semester = semesterService.getSemesterByDetails(seesion, semesterSemester, anne_etude);
	            

	            
	            if(module == null) {
	            	workbook.close();
	            	return "module introuvable";
	            }
	            if(semester == null) {
	            	workbook.close();
	            	return "semester introuvable";
	            }

	            

	            List<Matiere> ModulemMtieres = matiereService.getMatieresByModuleId(module.getId());
	            List<Matiere> orderedMatieres = new ArrayList<>();
	            
	            if(ModulemMtieres == null) {
	            	workbook.close();
	            	String message = "les matieres de ce module n'est pas trouve";
	            	return message;
	            }
	            
	            int matiereCellIndex = 4;
	            Matiere m;
	            Row titresRow = sheet.getRow(3);
                for(int i = 0; i < ModulemMtieres.size(); i++) {
                	m = matiereService.getMatiereByTitre(titresRow.getCell(matiereCellIndex++).getStringCellValue());
                	orderedMatieres.add(m);
                	
                }
	            
	            // Iterate through rows starting from the fourth row (skip header)
                int colIndex;
	            for (int i = 4; i <= sheet.getLastRowNum(); i++) {
	            	Row row = sheet.getRow(i);
	            	
	            	Note note = new Note();
	            	NoteEtudiant ne = new NoteEtudiant();
	            	
	            	Etudiant etudiant = etudiantService.getEtudiantById((long) row.getCell(0).getNumericCellValue());
	            	double note_valeur = (double) row.getCell(orderedMatieres.size() + 4).getNumericCellValue();
	            	
	            	ne.setNote(note_valeur);
	            	ne.setSemester(semester);
	            	
	            	noteEtudiantService.addNoteEtudiant(ne, etudiant.getId(), module.getId());
	            	
	            	colIndex=4;
	                if (row == null) continue;

	                for(Matiere matiere: orderedMatieres) {
	                	float valeur = (float) row.getCell(colIndex++).getNumericCellValue();
	            
	                	note.setValeur(valeur);
	                	noteService.addNote(note, etudiant.getId(), semester.getId(), matiere.getId());
	                }
	                
	                
	
	            }
            	workbook.close();
            	 return "le fichier est traité avec succès";
	            }
	        

	 }
	
	 
	 
	 public byte[] generateDeliberationFile(Long id_niveau) {
		    Niveau niveau = niveauService.getNiveauById(id_niveau);
		    List<Module> modules = moduleService.getModulesByNiveauId(id_niveau);
		    List<Etudiant> etudiants = etudiantService.getEtudiantsByNiveau(id_niveau);

		    Workbook workbook = new XSSFWorkbook();
		    Sheet sheet = workbook.createSheet("collect-notes-par-module");

		    // Create header rows
		    Row headerRow1 = sheet.createRow(0);
		    headerRow1.createCell(0).setCellValue("Academic Year");
		    headerRow1.createCell(1);
		    headerRow1.createCell(2).setCellValue("Deliberation Date");
		    headerRow1.createCell(3);

		    Row headerRow2 = sheet.createRow(1);
		    headerRow2.createCell(0).setCellValue("Level");
		    headerRow2.createCell(1).setCellValue(niveau.getAlias());
		    // Empty row
		    sheet.createRow(2);

		    // Main headers row
		    Row headerRow3 = sheet.createRow(3);
		    headerRow3.createCell(0).setCellValue("ID");
		    headerRow3.createCell(1).setCellValue("CNE");
		    headerRow3.createCell(2).setCellValue("NOM");
		    headerRow3.createCell(3).setCellValue("PRENOM");

		    int moduleIndex = 4;
		    for (Module module : modules) {
		        Cell cell = headerRow3.createCell(moduleIndex++);
		        cell.setCellValue(module.getTitre());
		        // Merged cell
		        sheet.addMergedRegion(new CellRangeAddress(3, 3, moduleIndex - 1, moduleIndex + matiereService.getMatieresByModuleId(module.getId()).size()));
		    }

		    // Subject headers for each module
		    Row matieresHeaders = sheet.createRow(4);
		    for (Module module : modules) {
		        int matiereIndex = 4;
		        List<Matiere> matieres = matiereService.getMatieresByModuleId(module.getId());

		        for (Matiere matiere : matieres) {
		            matieresHeaders.createCell(matiereIndex++).setCellValue(matiere.getTitre());
		        }
		        matieresHeaders.createCell(matiereIndex++).setCellValue("Average"); // Average
		        matieresHeaders.createCell(matiereIndex++).setCellValue("Validation"); // Validation
		    }

		    int rowNum = 5;
		    for (Etudiant etudiant : etudiants) {
		        Row row = sheet.createRow(rowNum);
		        int cellIndex = 0;

		        // Student information
		        row.createCell(cellIndex++).setCellValue(etudiant.getId());
		        row.createCell(cellIndex++).setCellValue(etudiant.getCne());
		        row.createCell(cellIndex++).setCellValue(etudiant.getNom());
		        row.createCell(cellIndex++).setCellValue(etudiant.getPrenom());

		        for (Module module : modules) {
		            List<Matiere> matieres = matiereService.getMatieresByModuleId(module.getId());
		            int startMatiereIndex = cellIndex; // First subject column index for this module

		            for (Matiere matiere : matieres) {
		                List<Note> notes = noteService.getNoteByEtudiantAndMatiere(etudiant, matiere);
		                Note note = notes.stream()
		                        .filter(n -> n.getSemester().getSemester().equalsIgnoreCase("rattrapage"))
		                        .findFirst()
		                        .orElseGet(() ->
		                                notes.stream()
		                                        .filter(n -> !n.getSemester().getSemester().equalsIgnoreCase("rattrapage"))
		                                        .findFirst()
		                                        .orElse(null)
		                        );

		                row.createCell(cellIndex++).setCellValue(note != null ? note.getValeur() : 0.0);
		            }

		            int endMatiereIndex = cellIndex - 1; // Last subject column index for this module

		            // Average formula
		            String averageFormula = "AVERAGE(" + getColumnLetter(startMatiereIndex) + (rowNum + 1) + ":" + getColumnLetter(endMatiereIndex) + (rowNum + 1) + ")";
		            Cell averageCell = row.createCell(cellIndex++);
		            averageCell.setCellFormula(averageFormula);

		            // Validation formula
		            String validationFormula = "IF(" + getColumnLetter(cellIndex - 1) + (rowNum + 1) + ">=10, \"V\", \"NV\")";
		            Cell validationCell = row.createCell(cellIndex++);
		            validationCell.setCellFormula(validationFormula);
		        }

		        rowNum++;
		    }

		    // Convert workbook to byte array
		    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		    try {
		        workbook.write(outputStream);
		        workbook.close();
		    } catch (IOException e) {
		        e.printStackTrace();
		    }

		    return outputStream.toByteArray();
		}

		/**
		 * Converts column index to Excel column letter (e.g., 1 -> A, 2 -> B, ..., 27 -> AA)
		 */
		private String getColumnLetter(int columnIndex) {
		    StringBuilder columnLetter = new StringBuilder();
		    while (columnIndex >= 0) {
		        columnLetter.insert(0, (char) ('A' + (columnIndex % 26)));
		        columnIndex = (columnIndex / 26) - 1;
		    }
		    return columnLetter.toString();
		}

	 
	 
	 public String importer_deliberation_note(MultipartFile file) throws IOException{
		 
		 if(!file.getContentType().equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")) {
	         	return "Type de fichier non valide. Veuillez télécharger un fichier Excel.";
			 }
		 
	        try (InputStream inputStream = file.getInputStream()) {
	        	
	        	Workbook workbook = new XSSFWorkbook(inputStream);
	            Sheet sheet = workbook.getSheetAt(0);
	            
	            if(sheet.getRow(0) == null || sheet.getRow(2) == null || sheet.getRow(2) == null) {
	            	workbook.close();
	            	return "la structure n'est pas correcte";
	            }
	            
	            
	            String anneEtude = sheet.getRow(0).getCell(1).getStringCellValue();
	            if(anneEtude == null) {
	            	workbook.close();
	            	return "anne d'etude n'est pas trouvee";
	            }
	            String niveauAlias = sheet.getRow(1).getCell(1).getStringCellValue();
	            if(niveauAlias == null) {
	            	workbook.close();
	            	return "niveau n'est pas trouvee";
	            }
	   		 	Niveau niveau = niveauService.getNiveauByAlias(niveauAlias);
	   			List<Module> modules = moduleService.getModulesByNiveauId(niveau.getId());
	            
	   			double note;
	            for(int i = 5; i<=sheet.getLastRowNum(); i++) {
	            	Row row = sheet.getRow(i);
	            	Long id_etudiant = (long) row.getCell(0).getNumericCellValue();
	            	
	            	for(Module module: modules) {
	            		List<Matiere> matieres = matiereService.getMatieresByModuleId(module.getId());
	            		
	            		note = (double) row.getCell(matieres.size() + 4).getNumericCellValue();
	            		NoteFinal fn = new NoteFinal();

	            		fn.setNote(note);
	            		noteFinalService.addNote(fn, etudiantService.getEtudiantById(id_etudiant), module);

	            	}
	            }

	            workbook.close();
	            return "le fichier est traité avec succès";
	         }
	 }
	 
	 
	 
	
	 public void importer_inscription_reinscription (MultipartFile file) throws IOException{
		 
		 try (InputStream inputStream = file.getInputStream()) {
			 Workbook workbook = new XSSFWorkbook(inputStream);
	         Sheet sheet = workbook.getSheetAt(0);
	         
	         for(int i = 1; i<=sheet.getLastRowNum(); i++) {
	        	 
	        	Row row = sheet.getRow(i);
	        	
	            Long id_etudiant = (long) row.getCell(0).getNumericCellValue();
	            String cne = row.getCell(1).getStringCellValue();
	            String nom = row.getCell(2).getStringCellValue();
	            String prenom = row.getCell(3).getStringCellValue();
	            Long id_niveau = (long) row.getCell(4).getNumericCellValue();
	            String type = row.getCell(5).getStringCellValue();
	            
	            if(type.equals("INSCRIPTION")) {
	            	Etudiant etudiant = new Etudiant();
	            	etudiant.setCne(cne);
	            	etudiant.setNom(nom);
	            	etudiant.setPrenom(prenom);
	            	etudiantService.addEtudiant(etudiant, id_niveau);
	            }else {
	            	Etudiant etudiant = etudiantService.getEtudiantById(id_etudiant);
	            	etudiant.setNiveau(niveauService.getNiveauById(id_niveau));
	            }
	            
	            
	         }
	         workbook.close();
		 }
	 }
	 
	 
	 
	 
	 
	 
	 public String importer_sp (MultipartFile file) throws IOException{
		 
		 if(!file.getContentType().equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")) {
	         	return "Type de fichier non valide. Veuillez télécharger un fichier Excel.";
		}
		 
		 try (InputStream inputStream = file.getInputStream()) {
			 Workbook workbook = new XSSFWorkbook(inputStream);
			 
			 if(workbook.getNumberOfSheets() != 5) {
				 workbook.close();
				 return "le fichier doit etre contenu 5 sheets";
			 }
	         Sheet coordinateur_sheet = workbook.getSheetAt(0);
	         Sheet filiere_sheet = workbook.getSheetAt(1);
	         Sheet niveaux_sheet = workbook.getSheetAt(2);
	         Sheet modules_sheet = workbook.getSheetAt(3);
	         Sheet matiere_sheet = workbook.getSheetAt(4);
	         
	         // traitmenet de cooredinateur filesheet
	         Enseignant cord = null;
	         for(int i = 1; i<=coordinateur_sheet.getLastRowNum(); i++) {	        	 
	        	 Row cord_row = coordinateur_sheet.getRow(i);
	        	 
	        	 String nom = cord_row.getCell(0).getStringCellValue();
	        	 String prenom = cord_row.getCell(1).getStringCellValue();
	        	 String estCord = cord_row.getCell(2).getStringCellValue();
	        	 
	        	 
	        	 Enseignant ens = new Enseignant(nom, prenom);
	        	 
	        	 if(estCord.equals("oui")) {
	        		 cord = ens;
	        	 }
	        	 
	        	 enseignantService.addEnseignant(ens);
	         }
	        	
	            
	            
	         //traitment de filiere filie sheet
	         Row filiere_row = filiere_sheet.getRow(1);
	         
	         String filiere_intitule = filiere_row.getCell(0).getStringCellValue();
	         String filiere_alias = filiere_row.getCell(1).getStringCellValue();
	         String anne_accreditation = String.valueOf(filiere_row.getCell(2).getNumericCellValue());
	         String anne_fin_accreditation = String.valueOf(filiere_row.getCell(3).getNumericCellValue());
	         float x = (float) filiere_row.getCell(4).getNumericCellValue();
	         float y = (float) filiere_row.getCell(5).getNumericCellValue();
	         
	         Filiere filiere = new Filiere(filiere_alias,filiere_intitule,anne_accreditation,anne_fin_accreditation, x, y);
	         filiereService.addFiliere(filiere, cord.getId());
	         
	         //traitment de niveaux filesheet
	         
	         for(int i = 1; i<=niveaux_sheet.getLastRowNum(); i++) {
	        	 Row row = niveaux_sheet.getRow(i);
	        	 String intitule = row.getCell(0).getStringCellValue();
		         String alias = row.getCell(1).getStringCellValue();
		         String niveau_suivant = row.getCell(2).getStringCellValue();
		         
		         if(niveau_suivant.equals("null")) {
		        	 niveauService.createNiveau(new Niveau(alias,intitule), filiere.getId(), null);
		         }else {
		        	 Niveau niveau = niveauService.getNiveauByAlias(niveau_suivant);
		        	 niveauService.createNiveau(new Niveau(alias,intitule), filiere.getId(), niveau.getId());
		         }
	         }
	         
	         //traitment de modules filesheet
	         for(int i = 1; i<=modules_sheet.getLastRowNum(); i++) {
	        	 Row row = modules_sheet.getRow(i);
	        	 String titre = row.getCell(0).getStringCellValue();
	        	 String code = row.getCell(1).getStringCellValue();
	        	 String alias_niveau = row.getCell(2).getStringCellValue();
	        	 String nom = row.getCell(3).getStringCellValue();
	        	 String prenom = row.getCell(4).getStringCellValue();
	        	 Enseignant ens = enseignantService.getEnseignantByNomAndPrenom(nom, prenom);
	        	 Module module = new Module(code, titre);
//	        	 enseignantService.
	        	 moduleService.createModule(module, niveauService.getNiveauByAlias(alias_niveau).getId(), ens.getId());
	         }
	         
	         //traitment de matieres filesheet
	         for(int i = 1; i<=matiere_sheet.getLastRowNum(); i++) {
	        	 Row row = matiere_sheet.getRow(i);
	        	 String titre = row.getCell(0).getStringCellValue();
	        	 String codeModule = row.getCell(1).getStringCellValue();
	        	 String nom = row.getCell(2).getStringCellValue();
	        	 String prenom = row.getCell(3).getStringCellValue();
	        	 
	        	 Enseignant ens = enseignantService.getEnseignantByNomAndPrenom(nom, prenom);
	        	 
	        	 matiereService.createMatiere(new Matiere(titre), moduleService.getModuleBycode(codeModule).getId(), ens.getId());
	         }
	         workbook.close();
	         return "le fichier est traité avec succès";
	         }
		 
		 }
		 
	 
	 
	 
	 
	 
	 
}
