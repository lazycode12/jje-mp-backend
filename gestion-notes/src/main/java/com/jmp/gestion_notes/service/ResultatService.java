package com.jmp.gestion_notes.service;

import com.jmp.gestion_notes.model.Module;
import com.jmp.gestion_notes.model.Resultat;
import com.jmp.gestion_notes.repo.ResultatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ResultatService {

    @Autowired
    private ResultatRepository resultatRepository;

    @Autowired
    private ModuleService moduleService;
    
    
    // Add a new result
    public Resultat addResultat(Resultat resultat) {
        return resultatRepository.save(resultat);
    }

    // Get all results for a student
    public List<Resultat> getResultatsByEtudiant(Long idEtudiant) {
        return resultatRepository.findByEtudiantId(idEtudiant);
    }

    // Check if a student is eligible for a level
    public boolean estEligibleAuNiveau(Long idEtudiant, Long idNiveau) {
        List<Resultat> resultats = resultatRepository.findByEtudiantId(idEtudiant);
        return resultats.stream()
                .filter(resultat -> resultat.getNiveau().getId().equals(idNiveau))
                .allMatch(Resultat::isValide);
    }

    // Check if a student is ajourné (failed)
    public boolean estEtudiantAjourne(Long idEtudiant) {
        List<Resultat> resultats = resultatRepository.findByEtudiantId(idEtudiant);
        return resultats.stream()
                .anyMatch(resultat -> !resultat.isValide());
    }

    public List<Module> getModulesNonValides(Long idEtudiant) {
        List<Resultat> resultats = resultatRepository.findByEtudiantId(idEtudiant);
        List<com.jmp.gestion_notes.model.Module> modules = moduleService.getModulesByNiveauId(resultats.get(0).getNiveau().getId());

        return modules.stream()
                .filter(module -> resultats.stream()
                        .anyMatch(resultat -> resultat.getNiveau().getId().equals(module.getNiveau().getId()) && !resultat.isValide()))
                .collect(Collectors.toList());
    }

    // Verify previous enrollments for a student
    public boolean verifierInscriptionsPrecedentes(Long idEtudiant, Long idNiveau) {
        List<Resultat> resultats = resultatRepository.findByEtudiantId(idEtudiant);
        return resultats.stream()
                .anyMatch(resultat -> resultat.getNiveau().getId().equals(idNiveau));
    }

    public boolean isNiveauConsistent(Long idEtudiant, Long idNiveau) {
        // Fetch all results for the student
        List<Resultat> resultats = resultatRepository.findByEtudiantId(idEtudiant);

        // Check if the student has any results for the given niveau
        boolean hasResultsForNiveau = resultats.stream()
                .anyMatch(resultat -> resultat.getNiveau().getId().equals(idNiveau));

        // Check if the student has passed all modules for the given niveau
        boolean hasPassedAllModules = resultats.stream()
                .filter(resultat -> resultat.getNiveau().getId().equals(idNiveau))
                .allMatch(Resultat::isValide);

        // The niveau is consistent if the student has results for it and has passed all modules
        return hasResultsForNiveau && hasPassedAllModules;
    }
}