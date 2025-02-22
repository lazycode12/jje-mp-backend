package com.jmp.gestion_notes.utils;

import com.jmp.gestion_notes.model.Etudiant;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

public class ExcelUtils {

    public static Long getCellValueAsLong(Row row, int cellIndex) {
        Cell cell = row.getCell(cellIndex);
        if (cell == null) {
            return null;
        }
        return (long) cell.getNumericCellValue();
    }

    public static String getCellValueAsString(Row row, int cellIndex) {
        Cell cell = row.getCell(cellIndex);
        if (cell == null) {
            return null;
        }
        return cell.getStringCellValue();
    }

    public static boolean isHeaderValid(Row headerRow) {
        // Validate the header row format
        return true; // Implement your validation logic here
    }

    public static byte[] generateExcelFile(List<Etudiant> etudiants) {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Etudiants");

            // Create header row
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("ID");
            headerRow.createCell(1).setCellValue("CNE");
            headerRow.createCell(2).setCellValue("Nom");
            headerRow.createCell(3).setCellValue("Prénom");
            headerRow.createCell(4).setCellValue("Niveau");

            // Fill data rows
            int rowNum = 1;
            for (Etudiant etudiant : etudiants) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(etudiant.getId());
                row.createCell(1).setCellValue(etudiant.getCne());
                row.createCell(2).setCellValue(etudiant.getNom());
                row.createCell(3).setCellValue(etudiant.getPrenom());
                row.createCell(4).setCellValue(etudiant.getNiveau().getAlias());
            }

            workbook.write(out);
            return out.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("Failed to generate Excel file: " + e.getMessage());
        }
    }
}