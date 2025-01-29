package com.jmp.gestion_notes.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Niveau {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String alias;
	private String intitule;
	
	@ManyToOne
	@JoinColumn(name="id_filiere")
	private Filiere filiere;
	
	@ManyToOne
	@JoinColumn(name="id_niveau_suivant")
	private Niveau niveauSuivant;
	
	// constructors
	public Niveau() {}

	public Niveau(String alias, String intitule) {
		this.alias = alias;
		this.intitule = intitule;
	}
	
	//getters and setters
	

	
	public Long getId() {
		return id;
	}

	public String getIntitule() {
		return intitule;
	}

	public void setIntitule(String intitule) {
		this.intitule = intitule;
	}

	public Filiere getFiliere() {
		return filiere;
	}

	public void setFiliere(Filiere filiere) {
		this.filiere = filiere;
	}

	public Niveau getNiveauSuivant() {
		return niveauSuivant;
	}

	public void setNiveauSuivant(Niveau niveauSuivant) {
		this.niveauSuivant = niveauSuivant;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	@Override
	public String toString() {
		return "Niveau [id=" + id + ", alias=" + alias + "]";
	}
	
	
	
	
	
	
}
