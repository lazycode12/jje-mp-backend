package com.jmp.gestion_notes.service;

import com.jmp.gestion_notes.exception.ResourceNotFoundException;
import com.jmp.gestion_notes.model.*;
import com.jmp.gestion_notes.repo.EtudiantRepository;
import com.jmp.gestion_notes.repo.NiveauRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EtudiantService {

    @Autowired
    private EtudiantRepository etudiantRepository;

    @Autowired
    private NiveauRepository niveauRepository;

    public List<Etudiant> getAllEtudiants() {
        return etudiantRepository.findAll();
    }

    public Etudiant getEtudiantById(Long id) {
        return etudiantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Etudiant", "id", id));
    }

    public Optional<Etudiant> getEtudiantByCne(String cne) {
        return etudiantRepository.findByCne(cne);
    }

    public Etudiant addEtudiant(Etudiant etudiant, Long idNiveau) {
        Niveau niveau = niveauRepository.findById(idNiveau)
                .orElseThrow(() -> new ResourceNotFoundException("Niveau", "id", idNiveau));
        etudiant.setNiveau(niveau);
        return etudiantRepository.save(etudiant);
    }

    public Etudiant updateEtudiant(Etudiant etudiant, Long idNiveau) {
        Etudiant existingEtudiant = getEtudiantById(etudiant.getId());
        existingEtudiant.setNom(etudiant.getNom());
        existingEtudiant.setPrenom(etudiant.getPrenom());
        existingEtudiant.setCne(etudiant.getCne());

        if (idNiveau != null) {
            Niveau niveau = niveauRepository.findById(idNiveau)
                    .orElseThrow(() -> new ResourceNotFoundException("Niveau", "id", idNiveau));
            existingEtudiant.setNiveau(niveau);
        }

        return etudiantRepository.save(existingEtudiant);
    }

    public void deleteEtudiant(Long id) {
        Etudiant etudiant = getEtudiantById(id);
        etudiantRepository.delete(etudiant);
    }

    public List<Etudiant> getEtudiantsByNiveau(Long niveauId) {
        return etudiantRepository.findByNiveauId(niveauId);
    }

    public List<Etudiant> searchEtudiants(CriteresRecherche criteres) {
        return etudiantRepository.findByCneOrNomOrPrenom(criteres.getCne(), criteres.getNom(), criteres.getPrenom());
    }
}