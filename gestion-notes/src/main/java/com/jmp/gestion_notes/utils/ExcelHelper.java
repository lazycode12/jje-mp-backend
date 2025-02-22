package com.jmp.gestion_notes.utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import com.jmp.gestion_notes.model.Etudiant;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ExcelHelper {

    public static boolean hasExcelFormat(MultipartFile file) {
        return file.getContentType().equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
    }

    public static List<Etudiant> excelToEtudiants(MultipartFile file) {
        List<Etudiant> etudiants = new ArrayList<>();

        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rows = sheet.iterator();

            boolean firstRow = true; // Ignorer l'entête
            while (rows.hasNext()) {
                Row currentRow = rows.next();
                if (firstRow) {
                    firstRow = false;
                    continue;
                }

                Etudiant etudiant = new Etudiant();
                etudiant.setId((long) currentRow.getCell(0).getNumericCellValue());
                etudiant.setCne(currentRow.getCell(1).getStringCellValue());
                etudiant.setNom(currentRow.getCell(2).getStringCellValue());
                etudiant.setPrenom(currentRow.getCell(3).getStringCellValue());

                etudiants.add(etudiant);
            }
        } catch (IOException e) {
            throw new RuntimeException("Erreur lors de la lecture du fichier Excel : " + e.getMessage());
        }

        return etudiants;
    }
}
