package com.jmp.gestion_notes.service;

import com.jmp.gestion_notes.model.*;
import com.jmp.gestion_notes.model.Module;
import com.jmp.gestion_notes.utils.ExcelUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

@Service
public class InscriptionService {

    @Autowired
    private EtudiantService etudiantService;

    @Autowired
    private NiveauService niveauService;

    @Autowired
    private ModuleService moduleService;

    @Autowired
    private InscriptionModuleService inscriptionModuleService;

    @Autowired
    private ResultatService resultatService;

    @Transactional
    public void importerInscriptionsEtReinscriptions(MultipartFile file) throws IOException {
        try (InputStream inputStream = file.getInputStream(); Workbook workbook = new XSSFWorkbook(inputStream)) {
            Sheet sheet = workbook.getSheetAt(0);
            if (sheet == null) {
                throw new IllegalArgumentException("Le fichier est vide ou ne contient pas de feuilles valides.");
            }
            validateFileFormat(sheet);
            processRows(sheet);
        }
    }

    private void validateFileFormat(Sheet sheet) {
        Row headerRow = sheet.getRow(0);
        if (headerRow == null || !ExcelUtils.isHeaderValid(headerRow)) {
            throw new IllegalArgumentException("Format de fichier incorrect. Vérifiez les en-têtes des colonnes.");
        }
    }

    private void processRows(Sheet sheet) {
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row != null) {
                try {
                    processRow(row);
                } catch (Exception e) {
                    System.err.println("Erreur ligne " + (i + 1) + " : " + e.getMessage());
                }
            }
        }
    }

    public void processRow(Row row) {
        Long idEtudiant = ExcelUtils.getCellValueAsLong(row, 0);
        String cne = ExcelUtils.getCellValueAsString(row, 1);
        String nom = ExcelUtils.getCellValueAsString(row, 2);
        String prenom = ExcelUtils.getCellValueAsString(row, 3);
        Long idNiveau = ExcelUtils.getCellValueAsLong(row, 4);
        String type = ExcelUtils.getCellValueAsString(row, 5);

        validateRequiredFields(cne, nom, prenom, idNiveau, type);

        Optional<Etudiant> etudiantOptional = etudiantService.getEtudiantByCne(cne);

        switch (type.toUpperCase()) {
            case "INSCRIPTION":
                if (etudiantOptional.isPresent()) {
                    throw new IllegalArgumentException("L'étudiant avec le CNE " + cne + " existe déjà. Utilisez REINSCRIPTION.");
                }
                traiterInscription(cne, nom, prenom, idNiveau);
                break;

            case "REINSCRIPTION":
                if (etudiantOptional.isEmpty()) {
                    throw new IllegalArgumentException("L'étudiant avec le CNE " + cne + " n'existe pas. Utilisez INSCRIPTION.");
                }
                Etudiant etudiant = etudiantOptional.get();
                if (!resultatService.isNiveauConsistent(etudiant.getId(), idNiveau)) {
                    throw new IllegalArgumentException("Le niveau spécifié est contradictoire avec les résultats ou inscriptions précédentes.");
                }
                traiterReinscription(etudiant.getId(), cne, nom, prenom, idNiveau);
                break;

            default:
                throw new IllegalArgumentException("Type d'inscription inconnu : " + type);
        }
    }

    private void validateRequiredFields(String cne, String nom, String prenom, Long idNiveau, String type) {
        if (cne == null || nom == null || prenom == null || idNiveau == null || type == null) {
            throw new IllegalArgumentException("Données manquantes dans la ligne.");
        }
    }

    private void traiterInscription(String cne, String nom, String prenom, Long idNiveau) {
        Etudiant etudiant = new Etudiant();
        etudiant.setCne(cne);
        etudiant.setNom(nom);
        etudiant.setPrenom(prenom);
        etudiantService.addEtudiant(etudiant, idNiveau);
        inscrireEtudiantDansModules(etudiant, idNiveau);
    }

    private void traiterReinscription(Long idEtudiant, String cne, String nom, String prenom, Long idNiveau) {
        Etudiant etudiant = etudiantService.getEtudiantById(idEtudiant);
        if (etudiant == null) {
            throw new IllegalArgumentException("Étudiant non trouvé avec l'ID : " + idEtudiant);
        }

        if (!etudiant.getCne().equals(cne) || !etudiant.getNom().equals(nom) || !etudiant.getPrenom().equals(prenom)) {
            etudiant.setCne(cne);
            etudiant.setNom(nom);
            etudiant.setPrenom(prenom);
            etudiantService.updateEtudiant(etudiant, idNiveau);
        }
        inscrireEtudiantDansModules(etudiant, idNiveau);
    }

    private void inscrireEtudiantDansModules(Etudiant etudiant, Long idNiveau) {
        List<Module> modules = moduleService.getModulesByNiveauId(idNiveau);
        boolean estAjourne = resultatService.estEtudiantAjourne(etudiant.getId());

        if (estAjourne) {
            List<Module> modulesNonValides = resultatService.getModulesNonValides(etudiant.getId());
            modules.stream()
                   .filter(modulesNonValides::contains)
                   .forEach(module -> inscriptionModuleService.inscrireEtudiant(etudiant, module));
        } else {
            modules.forEach(module -> inscriptionModuleService.inscrireEtudiant(etudiant, module));
        }
    }

    public void inscrireEtudiantAjourne(Long idEtudiant, Long idNiveau) {
        Etudiant etudiant = etudiantService.getEtudiantById(idEtudiant);
        if (etudiant == null) {
            throw new IllegalArgumentException("Étudiant non trouvé avec l'ID : " + idEtudiant);
        }
        inscrireEtudiantDansModules(etudiant, idNiveau);
    }

    public void inscrireModulesNiveauSuivant(Long idEtudiant, List<Long> idModules) {
        Etudiant etudiant = etudiantService.getEtudiantById(idEtudiant);
        if (!resultatService.estEtudiantAjourne(idEtudiant)) {
            throw new IllegalStateException("Seuls les étudiants ajournés peuvent s'inscrire aux modules du niveau supérieur");
        }

        Long idNiveauSuivant = niveauService.getNiveauSuivant(etudiant.getNiveau().getId());
        idModules.stream()
                  .map(moduleService::getModuleById)
                  .filter(module -> module.getNiveau().getId().equals(idNiveauSuivant))
                  .forEach(module -> inscriptionModuleService.inscrireEtudiant(etudiant, module));
    }

    public List<Etudiant> rechercherEtudiants(CriteresRecherche criteres) {
        return etudiantService.searchEtudiants(criteres);
    }

    public List<Etudiant> getEtudiantsParClasse(Long idNiveau) {
        return etudiantService.getEtudiantsByNiveau(idNiveau);
    }

    public byte[] exporterListeEtudiants() {
        List<Etudiant> etudiants = etudiantService.getAllEtudiants();
        return ExcelUtils.generateExcelFile(etudiants);
    }
}