package com.jmp.gestion_notes.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Module {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String code, titre;
	
	@ManyToOne
	@JoinColumn(name="id_niveau")
	private Niveau niveau;
	
	@ManyToOne
	@JoinColumn(name="id_responsable")
	private Enseignant resp;
	
	public Module() {
		super();
	}

	public Module(String code, String titre) {
		super();
		this.code = code;
		this.titre = titre;
	}
	
	

	public Enseignant getResp() {
		return resp;
	}

	public void setResp(Enseignant resp) {
		this.resp = resp;
	}

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

	@Override
	public String toString() {
		return "Module [id=" + id + ", code=" + code + ", titre=" + titre + "]";
	}
	
	
	
	
	
	
}
