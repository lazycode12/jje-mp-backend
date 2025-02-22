package com.jmp.gestion_notes.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
public class Module {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String code, titre;

    @ManyToOne
    @JoinColumn(name = "id_niveau")
    private Niveau niveau;

    @ManyToOne
    @JoinColumn(name = "id_responsable")
    private Enseignant resp;

    @OneToMany(mappedBy = "module", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<NoteEtudiant> notes = new ArrayList<>();

    @OneToMany(mappedBy = "module", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Matiere> matieres = new ArrayList<>();

    public Module() {
        super();
    }

    public Module(String code, String titre) {
        super();
        this.code = code;
        this.titre = titre;
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public Niveau getNiveau() {
        return niveau;
    }

    public void setNiveau(Niveau niveau) {
        this.niveau = niveau;
    }

    public Enseignant getResp() {
        return resp;
    }

    public void setResp(Enseignant resp) {
        this.resp = resp;
    }

    public List<NoteEtudiant> getNotes() {
        return notes;
    }

    public void setNotes(List<NoteEtudiant> notes) {
        this.notes = notes;
    }

    public List<Matiere> getMatieres() {
        return matieres;
    }

    public void setMatieres(List<Matiere> matieres) {
        this.matieres = matieres;
    }

    @Override
    public String toString() {
        return "Module [id=" + id + ", code=" + code + ", titre=" + titre + "]";
    }
}