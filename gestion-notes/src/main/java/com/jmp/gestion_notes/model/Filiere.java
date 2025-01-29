package com.jmp.gestion_notes.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Filiere {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String alias, intitule, anne_accreditation, anne_fin_accreditation;
	private float x, y;
	
	@ManyToOne
	@JoinColumn(name="id_coordinateur")
	private Enseignant coordinateur;
	
	// constructors
	public Filiere() {}

	public Filiere(String alias, String intitule, String anne_accreditation, String anne_fin_accreditation, float x, float y) {
		this.alias = alias;
		this.intitule = intitule;
		this.anne_accreditation = anne_accreditation;
		this.anne_fin_accreditation = anne_fin_accreditation;
		this.x = x;
		this.y = y;
	}
	
	// getters and setters
	
	
	public Long getId() {
		return id;
	}

	public Enseignant getCoordinateur() {
		return coordinateur;
	}

	public void setCoordinateur(Enseignant coordinateur) {
		this.coordinateur = coordinateur;
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

	public String getIntitule() {
		return intitule;
	}

	public void setIntitule(String intitule) {
		this.intitule = intitule;
	}

	public String getAnne_accreditation() {
		return anne_accreditation;
	}

	public void setAnne_accreditation(String anne_accreditation) {
		this.anne_accreditation = anne_accreditation;
	}

	public String getAnne_fin_accreditation() {
		return anne_fin_accreditation;
	}

	public void setAnne_fin_accreditation(String anne_fin_accreditation) {
		this.anne_fin_accreditation = anne_fin_accreditation;
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}
	
	// toString() method
	@Override
	public String toString() {
		return "Filiere [id=" + id + ", alias=" + alias + ", intitule=" + intitule + ", anne_accreditation="
				+ anne_accreditation + ", anne_fin_accreditation=" + anne_fin_accreditation + ", x=" + x + ", y=" + y
				+ "]";
	}
	
	
	
	
	
}
