package com.jmp.gestion_notes.service;

import com.jmp.gestion_notes.model.Etudiant;
import com.jmp.gestion_notes.model.Module;
import com.jmp.gestion_notes.model.NoteEtudiant;
import com.jmp.gestion_notes.repo.NoteEtudiantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InscriptionModuleService {

    @Autowired
    private NoteEtudiantRepository noteEtudiantRepository;

    /**
     * Inscrit un étudiant dans un module.
     *
     * @param etudiant L'étudiant à inscrire.
     * @param module   Le module dans lequel l'étudiant est inscrit.
     */
    public void inscrireEtudiant(Etudiant etudiant, Module module) {
        // Vérifier si l'étudiant est déjà inscrit dans ce module
        NoteEtudiant noteEtudiantExistant = noteEtudiantRepository.findByEtudiantAndModule(etudiant, module);
        if (noteEtudiantExistant != null) {
            throw new IllegalArgumentException("L'étudiant est déjà inscrit dans ce module.");
        }

        // Créer une nouvelle inscription
        NoteEtudiant noteEtudiant = new NoteEtudiant();
        noteEtudiant.setEtudiant(etudiant);
        noteEtudiant.setModule(module);
        noteEtudiant.setNote(null); // La note est initialisée à null
        noteEtudiant.setAnneEtude("2023-2024"); // Exemple d'année d'étude

        // Enregistrer l'inscription dans la base de données
        noteEtudiantRepository.save(noteEtudiant);
    }
}