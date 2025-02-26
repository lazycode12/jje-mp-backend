package com.jmp.gestion_notes.service;

import com.jmp.gestion_notes.model.*;


import com.jmp.gestion_notes.model.Module;
import com.jmp.gestion_notes.repo.EnseignantRepository;
import com.jmp.gestion_notes.repo.MatiereRepository;
import com.jmp.gestion_notes.repo.ModuleRepository;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Iterator;
@Service
public class ExcelGenerationService {

    @Autowired
    private ModuleService moduleService;

    @Autowired
    private EtudiantService etudiantService;

    @Autowired
    private NiveauService niveauService;

    @Autowired
    private MatiereService matiereService;
    
    @Autowired
    private NoteService noteService;
    @Autowired
    private ModuleRepository moduleRepository;
    @Autowired
    private EnseignantRepository enseignantRepository;
    
    @Autowired
    private MatiereRepository matiereRepository;
    
    
    public byte[] generateModuleGradesFile(Long moduleId, SessionType session) throws IOException {
        Module module = moduleService.getModuleById(moduleId);
        List<Etudiant> etudiants = etudiantService.getEtudiantsByNiveau(module.getNiveau().getId());

        // Fetch thresholds (X and Y) from the database
        double thresholdX = module.getNiveau().getThresholdX(); // e.g., 12
        double thresholdY = module.getNiveau().getThresholdY(); // e.g., 8

        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet(module.getTitre());

            // Create header row
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("ID");
            headerRow.createCell(1).setCellValue("CNE");
            headerRow.createCell(2).setCellValue("NOM");
            headerRow.createCell(3).setCellValue("PRENOM");

            // Add columns for each Matiere in the module
            int colIndex = 4;
            for (Matiere matiere : module.getMatieres()) {
                headerRow.createCell(colIndex++).setCellValue(matiere.getTitre());
            }

            // Add columns for Moyenne and Validation
            headerRow.createCell(colIndex).setCellValue("Moyenne");
            headerRow.createCell(colIndex + 1).setCellValue("Validation");

            // Fill data rows
            int rowIndex = 1;
            for (Etudiant etudiant : etudiants) {
                Row row = sheet.createRow(rowIndex++);
                row.createCell(0).setCellValue(etudiant.getId());
                row.createCell(1).setCellValue(etudiant.getCne());
                row.createCell(2).setCellValue(etudiant.getNom());
                row.createCell(3).setCellValue(etudiant.getPrenom());

                // Add empty cells for Matiere grades
                for (int i = 0; i < module.getMatieres().size(); i++) {
                    row.createCell(4 + i).setCellValue("");
                }

                // Add formulas for Moyenne and Validation
                Cell moyenneCell = row.createCell(4 + module.getMatieres().size());
                moyenneCell.setCellFormula("AVERAGE(D" + (rowIndex) + ":H" + (rowIndex) + ")"); // Adjust range as needed

                Cell validationCell = row.createCell(4 + module.getMatieres().size() + 1);
                if (session == SessionType.NORMAL) {
                    validationCell.setCellFormula("IF(I" + (rowIndex) + ">=" + thresholdX + ", \"V\", IF(I" + (rowIndex) + ">=" + thresholdY + ", \"R\", \"NV\"))");
                } else {
                    validationCell.setCellFormula("IF(I" + (rowIndex) + ">=" + thresholdY + ", \"V\", \"NV\")");
                }
            }

            workbook.write(out);
            return out.toByteArray();
        }
    }


    
    
    
    public byte[] generateDeliberationFile(Long niveauId) throws IOException {
        Niveau niveau = niveauService.getNiveauById(niveauId);
        if (niveau == null) {
            throw new IllegalArgumentException("Niveau with ID " + niveauId + " not found");
        }

        List<Etudiant> etudiants = etudiantService.getEtudiantsByNiveau(niveauId);
        if (etudiants.isEmpty()) {
            throw new IllegalArgumentException("No students found for niveau ID " + niveauId);
        }

        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Deliberation");

            // Create header row
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("ID");
            headerRow.createCell(1).setCellValue("CNE");
            headerRow.createCell(2).setCellValue("NOM");
            headerRow.createCell(3).setCellValue("PRENOM");

            // Add columns for each module
            int colIndex = 4;
            for (Module module : niveau.getModules()) {
                headerRow.createCell(colIndex++).setCellValue(module.getTitre());
            }

            // Add columns for Moyenne and Rang
            headerRow.createCell(colIndex).setCellValue("Moyenne");
            headerRow.createCell(colIndex + 1).setCellValue("Rang");

            // Fill data rows
            int rowIndex = 1;
            for (Etudiant etudiant : etudiants) {
                Row row = sheet.createRow(rowIndex++);
                row.createCell(0).setCellValue(etudiant.getId());
                row.createCell(1).setCellValue(etudiant.getCne());
                row.createCell(2).setCellValue(etudiant.getNom());
                row.createCell(3).setCellValue(etudiant.getPrenom());

                // Add empty cells for module grades
                for (int i = 0; i < niveau.getModules().size(); i++) {
                    row.createCell(4 + i).setCellValue("");
                }

                // Add formulas for Moyenne and Rang
                Cell moyenneCell = row.createCell(4 + niveau.getModules().size());
                moyenneCell.setCellFormula("AVERAGE(D" + (rowIndex) + ":H" + (rowIndex) + ")"); // Adjust range as needed

                Cell rangCell = row.createCell(4 + niveau.getModules().size() + 1);
                rangCell.setCellFormula("RANK(I" + (rowIndex) + ",$I$2:$I$" + (etudiants.size() + 1) + ",0)");
            }

            workbook.write(out);
            return out.toByteArray();
        }
    }




    public void importMatiereModuleEnseignant(MultipartFile file) throws IOException {
        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator(); // Now this will work

            // Skip header row
            if (rowIterator.hasNext()) {
                rowIterator.next();
            }

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();

                // Read data from the Excel file
                String matiere = row.getCell(0).getStringCellValue();
                String module = row.getCell(1).getStringCellValue();
                String nomEnseignant = row.getCell(2).getStringCellValue();
                String prenomEnseignant = row.getCell(3).getStringCellValue();

                // Save Enseignant
                Enseignant enseignant = new Enseignant();
                enseignant.setNom(nomEnseignant);
                enseignant.setPrenom(prenomEnseignant);
                enseignant = enseignantRepository.save(enseignant);

                // Save Module
                Module newModule = new Module();
                newModule.setTitre(module);
                newModule.setResponsable(enseignant); // Assuming Module has a 'responsable' field of type Enseignant
                newModule = moduleRepository.save(newModule);

                // Save Matiere
                Matiere newMatiere = new Matiere();
                newMatiere.setTitre(matiere);
                newMatiere.setModule(newModule); // Set the Module object directly
                newMatiere.setEnseignant(enseignant); // Set the Enseignant object directly
                matiereRepository.save(newMatiere);
            }
        }
    }
    

    public void importModuleGrades(MultipartFile file, Long moduleId, SessionType session) throws IOException {
        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);

            // Validate file structure
            if (!validateFileStructure(sheet, moduleId)) {
                throw new IllegalArgumentException("Invalid file structure");
            }

            // Iterate through rows and validate grades
            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue; // Skip header row

                // Save grades to the database
                saveGradesToDatabase(row, moduleId, session);
            }
        }
    }

    
    private boolean validateFileStructure(Sheet sheet, Long moduleId) {
        Row headerRow = sheet.getRow(0);
        if (headerRow == null) {
            System.out.println("Header row is missing");
            return false;
        }

        // Validate header row
        if (!headerRow.getCell(0).getStringCellValue().equals("ID")) {
            System.out.println("Column 0 is not 'ID'");
            return false;
        }
        if (!headerRow.getCell(1).getStringCellValue().equals("CNE")) {
            System.out.println("Column 1 is not 'CNE'");
            return false;
        }
        if (!headerRow.getCell(2).getStringCellValue().equals("NOM")) {
            System.out.println("Column 2 is not 'NOM'");
            return false;
        }
        if (!headerRow.getCell(3).getStringCellValue().equals("PRENOM")) {
            System.out.println("Column 3 is not 'PRENOM'");
            return false;
        }

        // Validate module-specific columns
        Module module = moduleService.getModuleById(moduleId);
        if (module == null) {
            System.out.println("Module with ID " + moduleId + " not found");
            return false;
        }
        for (int i = 0; i < module.getMatieres().size(); i++) {
            String expectedTitle = module.getMatieres().get(i).getTitre();
            String actualTitle = headerRow.getCell(4 + i).getStringCellValue();
            if (!actualTitle.equals(expectedTitle)) {
                System.out.println("Column " + (4 + i) + " is not '" + expectedTitle + "'. Found: " + actualTitle);
                return false;
            }
        }

        return true;
    }

    
    
    private void saveGradesToDatabase(Row row, Long moduleId, SessionType session) {
        // Extract student ID from the row
        Long studentId = (long) row.getCell(0).getNumericCellValue();
        System.out.println("Processing student ID: " + studentId);

        // Extract grades for each Matiere
        Module module = moduleService.getModuleById(moduleId);
        List<Matiere> matieres = module.getMatieres();

        for (int i = 0; i < matieres.size(); i++) {
            Matiere matiere = matieres.get(i);
            Cell gradeCell = row.getCell(4 + i);

            // Handle both numeric and string cells
            double grade;
            if (gradeCell.getCellType() == CellType.NUMERIC) {
                grade = gradeCell.getNumericCellValue();
            } else if (gradeCell.getCellType() == CellType.STRING) {
                try {
                    // Replace comma with period for locale-specific formats
                    String gradeValue = gradeCell.getStringCellValue().replace(',', '.');
                    grade = Double.parseDouble(gradeValue);
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Invalid grade value in cell: " + gradeCell.getStringCellValue());
                }
            } else {
                throw new IllegalArgumentException("Invalid cell type for grade in column: " + (4 + i));
            }

            // Validate the grade
            if (grade < 0.0 || grade > 20.0) {
                throw new IllegalArgumentException("Grade must be between 0.0 and 20.0: " + grade);
            }

            // Create a new Note object
            Note note = new Note();
            note.setEtudiant(etudiantService.getEtudiantById(studentId));
            note.setMatiere(matiere);
            note.setNote(grade);
            note.setSession(session);

            // Log the note being saved
            System.out.println("Saving note: " + note);

            // Save the note to the database
            noteService.saveNote(note);
        }
    }

    
    
}