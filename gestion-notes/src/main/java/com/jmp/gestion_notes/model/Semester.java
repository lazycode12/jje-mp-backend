package com.jmp.gestion_notes.model;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class Semester {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String semester, session, anneEtude;
	
	@OneToMany(mappedBy = "semester")
	private List<Note> notes;
	
	public Semester() {}

	public Semester(String semester, String session, String anneEtude) {
		super();
		this.semester = semester;
		this.session = session;
		this.anneEtude = anneEtude;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSemester() {
		return semester;
	}

	public void setSemester(String semester) {
		this.semester = semester;
	}

	public String getSession() {
		return session;
	}

	public void setSession(String session) {
		this.session = session;
	}

	public String getanneEtude() {
		return anneEtude;
	}

	public void setanneEtude(String anneEtude) {
		this.anneEtude = anneEtude;
	}

	@Override
	public String toString() {
		return "Semester [id=" + id + ", semester=" + semester + ", session=" + session + ", anneEtude=" + anneEtude
				+ "]";
	}
	
	
	
	
}
