package com.jmp.gestion_notes.model;

import jakarta.persistence.*;

@Entity
public class Resultat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "etudiant_id", nullable = false)
    private Etudiant etudiant;  // Relationship with the Etudiant (Student)

    @ManyToOne
    @JoinColumn(name = "niveau_id", nullable = false)
    private Niveau niveau;  // Relationship with the Niveau (Level)

    @ManyToOne
    @JoinColumn(name = "module_id", nullable = false)
    private Module module;  // Relationship with the Module

    @Column(nullable = false)
    private Double note;  // Grade (note) of the student

    @Column(nullable = false)
    private boolean valide;  // Indicates whether the student passed the module

    // Constructors, Getters, Setters

    public Resultat() {
    }

    public Resultat(Etudiant etudiant, Niveau niveau, Module module, Double note, boolean valide) {
        this.etudiant = etudiant;
        this.niveau = niveau;
        this.module = module;
        this.note = note;
        this.valide = valide;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Etudiant getEtudiant() {
        return etudiant;
    }

    public void setEtudiant(Etudiant etudiant) {
        this.etudiant = etudiant;
    }

    public Niveau getNiveau() {
        return niveau;
    }

    public void setNiveau(Niveau niveau) {
        this.niveau = niveau;
    }

    public Module getModule() {
        return module;
    }

    public void setModule(Module module) {
        this.module = module;
    }

    public Double getNote() {
        return note;
    }

    public void setNote(Double note) {
        this.note = note;
    }

    public boolean isValide() {
        return valide;
    }

    public void setValide(boolean valide) {
        this.valide = valide;
    }

    @Override
    public String toString() {
        return "Resultat{" +
                "id=" + id +
                ", etudiant=" + etudiant.getNom() +  // Assuming Etudiant has a method getNom()
                ", niveau=" + niveau.getAlias() +  // Assuming Niveau has a method getNom()
                ", module=" + module.getTitre() +  // Assuming Module has a method getNom()
                ", note=" + note +
                ", valide=" + valide +
                '}';
    }
}