package com.jmp.gestion_notes.service;

import java.util.Comparator;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jmp.gestion_notes.model.Niveau;
import com.jmp.gestion_notes.model.Etudiant;
import com.jmp.gestion_notes.model.Filiere;
import com.jmp.gestion_notes.repo.NiveauRepository;
import com.jmp.gestion_notes.repo.FiliereRepository;
import com.jmp.gestion_notes.exception.ResourceNotFoundException;

@Service
public class NiveauService {

    private final NiveauRepository niveauRepository;
    private final FiliereRepository filiereRepository;

    @Autowired
    public NiveauService(NiveauRepository niveauRepository, FiliereRepository filiereRepository) {
        this.niveauRepository = niveauRepository;
        this.filiereRepository = filiereRepository;
    }
    
    // Create Niveau
    public Niveau createNiveau(Niveau niveau, Long idFiliere, Long idNiveauSuivant) {
        Filiere filiere = filiereRepository.findById(idFiliere)
            .orElseThrow(() -> new ResourceNotFoundException("Filiere", "id", idFiliere));
        niveau.setFiliere(filiere);

        if (idNiveauSuivant != null) {
            Niveau n = niveauRepository.findById(idNiveauSuivant)
                .orElseThrow(() -> new ResourceNotFoundException("Niveau", "id", idNiveauSuivant));
            niveau.setNiveauSuivant(n);
        } else {
            niveau.setNiveauSuivant(null);
        }
        return niveauRepository.save(niveau);
    }

    public List<Niveau> getAllNiveaux() {
        return niveauRepository.findAll();
    }

    public Niveau updateNiveau(Niveau updatedNiveau, Long id, Long idNiveauSuivant) {
        Niveau niveau = getNiveauById(id);
        niveau.setAlias(updatedNiveau.getAlias());
        niveau.setIntitule(updatedNiveau.getIntitule());

        if (idNiveauSuivant != null) {
            Niveau n = niveauRepository.findById(idNiveauSuivant)
                .orElseThrow(() -> new ResourceNotFoundException("Niveau", "id", idNiveauSuivant));
            niveau.setNiveauSuivant(n);
        } else {
            niveau.setNiveauSuivant(null);
        }

        return niveauRepository.save(niveau);
    }

    public Niveau getNiveauById(Long id) {
        return niveauRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Niveau", "id", id));
    }

    public Niveau getNiveauByAlias(String alias) {
        return niveauRepository.findByAlias(alias);
    }

    public void deleteNiveau(Long id) {
        Niveau niveau = getNiveauById(id);
        niveauRepository.delete(niveau);
    }

    public boolean estNiveauValidePourEtudiant(Etudiant etudiant, Long idNiveau) {
        if (etudiant.getNiveau() == null) {
            return false; // L'étudiant n'a pas de niveau attribué
        }

        Niveau niveau = niveauRepository.findById(idNiveau).orElse(null);
        if (niveau == null) {
            return false; // Le niveau spécifié n'existe pas
        }

        return niveau.getId().equals(etudiant.getNiveau().getId()) || estNiveauSuivant(etudiant, niveau);
    }

    private boolean estNiveauSuivant(Etudiant etudiant, Niveau niveau) {
        return etudiant.getNiveau().getNiveauSuivant() != null &&
               etudiant.getNiveau().getNiveauSuivant().equals(niveau);
    }

    public Niveau getNextNiveau(Niveau currentNiveau) {
        if (currentNiveau == null) return null;
        return currentNiveau.getNiveauSuivant();
    }

    public Long getNiveauSuivant(Long idNiveauCourant) {
        Niveau niveauCourant = getNiveauById(idNiveauCourant);
        if (niveauCourant == null || niveauCourant.getNiveauSuivant() == null) {
            throw new IllegalArgumentException("Niveau suivant non trouvé pour le niveau courant ID : " + idNiveauCourant);
        }
        return niveauCourant.getNiveauSuivant().getId();
    }
}
