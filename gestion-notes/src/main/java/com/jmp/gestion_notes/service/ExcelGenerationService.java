package com.jmp.gestion_notes.service;

import com.jmp.gestion_notes.model.*;
import com.jmp.gestion_notes.model.Module;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Service
public class ExcelGenerationService {

    @Autowired
    private ModuleService moduleService;

    @Autowired
    private EtudiantService etudiantService;

    @Autowired
    private NiveauService niveauService;

    /**
     * Generate an Excel file for a specific module and session.
     *
     * @param moduleId The ID of the module.
     * @param session  The session type (NORMAL or RATTRAPAGE).
     * @return A byte array representing the Excel file.
     */
    
    
    public byte[] generateModuleGradesFile(Long moduleId, SessionType session) throws IOException {
        Module module = moduleService.getModuleById(moduleId);
        List<Etudiant> etudiants = etudiantService.getEtudiantsByNiveau(module.getNiveau().getId());

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
            int moyenneColIndex = colIndex;
            headerRow.createCell(moyenneColIndex).setCellValue("Moyenne");
            int validationColIndex = colIndex + 1;
            headerRow.createCell(validationColIndex).setCellValue("Validation");

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

                // Dynamically calculate the range for Moyenne
                char startGradeCol = 'D'; // First column for grades
                char endGradeCol = (char) ('D' + module.getMatieres().size() - 1); // Last column for grades
                String moyenneFormula = "AVERAGE(" + startGradeCol + rowIndex + ":" + endGradeCol + rowIndex + ")";
                Cell moyenneCell = row.createCell(moyenneColIndex);
                moyenneCell.setCellFormula(moyenneFormula);

                // Dynamically calculate the column for Validation
                char moyenneCol = (char) ('D' + module.getMatieres().size());
                String validationFormula = "IF(" + moyenneCol + rowIndex + ">=12, \"V\", IF(" + moyenneCol + rowIndex + ">=8, \"R\", \"NV\"))";
                Cell validationCell = row.createCell(validationColIndex);
                validationCell.setCellFormula(validationFormula);
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
  
    
}