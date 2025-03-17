package com.jmp.gestion_notes.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "note_etudiant")
public class NoteEtudiant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_etudiant", nullable = false)
    @JsonIgnore
    private Etudiant etudiant;

    @ManyToOne
    @JoinColumn(name = "id_module", nullable = false)
    @JsonIgnore
    private Module module;
    
    @ManyToOne
    @JoinColumn(name = "semester_id", nullable = false)
    private Semester semester;

    private Double note; // Grade of the student in this module
    private String anneEtude;
    // Constructors
    public NoteEtudiant() {}

    public NoteEtudiant(Etudiant etudiant, Module module, Double note, Semester semester) {
        this.etudiant = etudiant;
        this.module = module;
        this.note = note;
        this.semester = semester;
    }

    // Getters and Setters
    
    public Long getId() { return id; }
    public Semester getSemester() {
		return semester;
	}

	public void setSemester(Semester semester) {
		this.semester = semester;
	}

	public void setId(Long id) { this.id = id; }

    public Etudiant getEtudiant() { return etudiant; }
    public void setEtudiant(Etudiant etudiant) { this.etudiant = etudiant; }

    public Module getModule() { return module; }
    public void setModule(Module module) { this.module = module; }

    public Double getNote() { return note; }
    public void setNote(Double note) { this.note = note; }

	public String getAnneEtude() { return anneEtude; }
	public void setAnneEtude(String anneEtude) { this.anneEtude = anneEtude; }
    
    
}