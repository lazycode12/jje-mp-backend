package com.jmp.gestion_notes.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.jmp.gestion_notes.exception.ResourceNotFoundException;
import com.jmp.gestion_notes.model.Personne;
import com.jmp.gestion_notes.repo.PersonneRepository;

public class PersonneService {
	private final PersonneRepository personneRepository;

	@Autowired
	public PersonneService(PersonneRepository personneRepository) {
		this.personneRepository = personneRepository;
	}
	
	// create Personne
	public Personne createPersonne(Personne Personne) {
		return personneRepository.save(Personne);
	}
	
	public List<Personne> getAllPersonnes(){
		return personneRepository.findAll();
	}
	
	public Personne updatePersonne(Personne updatePersonne, Long id) {
		Personne Personne = getPersonneById(id);
		
		Personne.setNom(updatePersonne.getNom());
		Personne.setPrenom(updatePersonne.getPrenom());
		Personne.setCin(updatePersonne.getCin());
		Personne.setEmail(updatePersonne.getEmail());
		Personne.setTele(updatePersonne.getTele());
		
		return personneRepository.save(Personne);
	}
	
	public Personne getPersonneById(long id) {
		return personneRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Personne", "id", id));
	}
	
	public void deletePersonne(Long id) {
		Personne Personne = getPersonneById(id);
		personneRepository.delete(Personne);
	}
}
