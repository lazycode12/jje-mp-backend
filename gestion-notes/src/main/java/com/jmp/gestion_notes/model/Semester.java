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
	private String semester, session, anne_etude;
	
	@OneToMany(mappedBy = "semester")
	private List<Note> notes;
	
	public Semester() {}

	public Semester(String semester, String session, String anne_etude) {
		super();
		this.semester = semester;
		this.session = session;
		this.anne_etude = anne_etude;
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

	public String getAnne_etude() {
		return anne_etude;
	}

	public void setAnne_etude(String anne_etude) {
		this.anne_etude = anne_etude;
	}

	@Override
	public String toString() {
		return "Semester [id=" + id + ", semester=" + semester + ", session=" + session + ", anne_etude=" + anne_etude
				+ "]";
	}
	
	
	
	
}
