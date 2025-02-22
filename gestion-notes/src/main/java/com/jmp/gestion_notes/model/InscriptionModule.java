package com.jmp.gestion_notes.model;

import jakarta.persistence.*;

@Entity
public class InscriptionModule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "etudiant_id", nullable = false)
    private Etudiant etudiant;

    @ManyToOne
    @JoinColumn(name = "module_id", nullable = false)
    private Module module;

    private double note; // The grade of the student in the module
    private boolean valide; // Whether the module is validated

    // Constructors
    public InscriptionModule() {
    }

    public InscriptionModule(Etudiant etudiant, Module module, double note, boolean valide) {
        this.etudiant = etudiant;
        this.module = module;
        this.note = note;
        this.valide = valide;
    }

    // Getters and Setters
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

    public Module getModule() {
        return module;
    }

    public void setModule(Module module) {
        this.module = module;
    }

    public double getNote() {
        return note;
    }

    public void setNote(double note) {
        this.note = note;
    }

    public boolean isValide() {
        return valide;
    }

    public void setValide(boolean valide) {
        this.valide = valide;
    }

    // toString method
    @Override
    public String toString() {
        return "InscriptionModule{" +
                "id=" + id +
                ", etudiant=" + etudiant +
                ", module=" + module +
                ", note=" + note +
                ", valide=" + valide +
                '}';
    }
}