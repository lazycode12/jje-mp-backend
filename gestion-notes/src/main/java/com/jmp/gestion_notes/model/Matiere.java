package com.jmp.gestion_notes.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
public class Matiere {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String titre;
	
	@ManyToOne
	@JoinColumn(name="id_module")
	private Module module;
	
	@OneToMany(mappedBy="matiere")
	@JsonIgnore
	List<Note> notes;
	
	@ManyToOne
	@JoinColumn(name="id_enseignant")
	private Enseignant enseignant;
	
	public Matiere() {}

	public Matiere(String titre) {
		this.titre = titre;
	}

	
	public Enseignant getEnseignant() {
		return enseignant;
	}

	public void setEnseignant(Enseignant enseignant) {
		this.enseignant = enseignant;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitre() {
		return titre;
	}

	public void setTitre(String titre) {
		this.titre = titre;
	}

	
	public Module getModule() {
		return module;
	}

	public void setModule(Module module) {
		this.module = module;
	}

	public List<Note> getNotes() {
		return notes;
	}

	public void setNotes(List<Note> notes) {
		this.notes = notes;
	}

	@Override
	public String toString() {
		return "Matiere [id=" + id + ", titre=" + titre + "]";
	}
	
	
}
