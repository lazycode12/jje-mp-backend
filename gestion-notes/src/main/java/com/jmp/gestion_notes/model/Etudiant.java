package com.jmp.gestion_notes.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;

//import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
public class Etudiant {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String cne, nom, prenom;
	
	@ManyToOne
	@JoinColumn(name="id_niveau")
	private Niveau niveau;
	
    @OneToMany(mappedBy = "etudiant", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<NoteEtudiant> notes = new ArrayList<>();
    
//	@OneToMany(mappedBy="etudiant")
//	@JsonIgnore
//	List<Note> notes;
//	
	public Etudiant() {}

	public Etudiant(String cne, String nom, String prenom) {
		this.cne = cne;
		this.nom = nom;
		this.prenom = prenom;
	}
	
	

	public Niveau getNiveau() {
		return niveau;
	}

	public void setNiveau(Niveau niveau) {
		this.niveau = niveau;
	}
	
	

//	public List<Note> getNotes() {
//		return notes;
//	}
//
//	public void setNotes(List<Note> notes) {
//		this.notes = notes;
//	}

	public List<NoteEtudiant> getNotes() {
		return notes;
	}

	public void setNotes(List<NoteEtudiant> notes) {
		this.notes = notes;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCne() {
		return cne;
	}

	public void setCne(String cne) {
		this.cne = cne;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getPrenom() {
		return prenom;
	}

	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}

	@Override
	public String toString() {
		return "Etudiant [id=" + id + ", cne=" + cne + ", nom=" + nom + ", prenom=" + prenom + "]";
	}
	
	
	
	
}
