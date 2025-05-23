package com.jmp.gestion_notes.model;

import com.jmp.gestion_notes.enums.Role;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Utilisateur {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String login, password;
	
	// user role
    @Column(nullable = true)
    @Enumerated(EnumType.STRING)
    private Role role;
	
	@Column(nullable = false, columnDefinition = "TINYINT(1) DEFAULT 1") // Default value is 1
	private boolean enabled;
	
	@Column(nullable = false, columnDefinition = "TINYINT(1) DEFAULT 0") // Default value is 0
	private boolean locked;
	
	@ManyToOne
	@JoinColumn(name="id_personne", nullable = false)
	private Personne personne;
	
	
	public Utilisateur() {}

	public Utilisateur(Role role) {
		this.role = role;
		this.enabled = true;  // Set enabled to true
	    this.locked = false;
	}
	
	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public boolean isLocked() {
		return locked;
	}

	public void setLocked(boolean locked) {
		this.locked = locked;
	}
	
	

	public Personne getPersonne() {
		return personne;
	}

	public void setPersonne(Personne personne) {
		this.personne = personne;
	}

	@Override
	public String toString() {
		return "Utilisateur [id=" + id + ", role=" + role + ", login=" + login + ", password=" + password + ", enabled="
				+ enabled + ", locked=" + locked + "]";
	}
	
	
	

}
