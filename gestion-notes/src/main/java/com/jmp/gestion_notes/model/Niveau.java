package com.jmp.gestion_notes.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

@Entity
public class Niveau {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String alias;
	private String intitule;
	


	@OneToOne
    @JoinColumn(name = "niveau_suivant_id")
    private Niveau niveauSuivant;

	
	@ManyToOne
	@JoinColumn(name = "id_filiere")
	private Filiere filiere;
	
	
	@OneToMany(mappedBy = "niveau")
	@JsonIgnore
	private List<Etudiant> etudiants;
	
	// constructors
	public Niveau() {}

	public Niveau(String alias, String intitule) {
		this.alias = alias;
		this.intitule = intitule;
	}
	
	 @OneToMany(mappedBy = "niveau", cascade = CascadeType.ALL, orphanRemoval = true)
	 @JsonIgnore
	    private List<Module> modules = new ArrayList<>();


	
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


	private int levelOrder;

	public int getLevelOrder() {
	    return levelOrder;
	}

	public List<Etudiant> getEtudiants() {
		return etudiants;
	}

	public void setEtudiants(List<Etudiant> etudiants) {
		this.etudiants = etudiants;
	}

	public void setLevelOrder(int levelOrder) {
		this.levelOrder = levelOrder;
	}
	




    public List<Module> getModules() {
        return modules;
    }

    public void setModules(List<Module> modules) {
        this.modules = modules;
    }

    @Override
    public String toString() {
        return "Niveau [id=" + id + ", alias=" + alias + ", intitule=" + intitule + "]";
    }

	
}
