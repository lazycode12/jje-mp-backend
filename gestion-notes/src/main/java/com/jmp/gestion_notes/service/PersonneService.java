package com.jmp.gestion_notes.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jmp.gestion_notes.exception.ResourceNotFoundException;
import com.jmp.gestion_notes.model.Personne;
import com.jmp.gestion_notes.repo.PersonneRepository;
import com.jmp.gestion_notes.repo.UtilisateurRepository;

@Service
public class PersonneService {
	private final PersonneRepository personneRepository;
	private final UtilisateurRepository utilisateurRepository;

	@Autowired
	public PersonneService(PersonneRepository personneRepository, UtilisateurRepository utilisateurRepository) {
		this.personneRepository = personneRepository;
		this.utilisateurRepository = utilisateurRepository;
	}
	
	// create Personne
	public Personne createPersonne(Personne personne) {
		return personneRepository.save(personne);
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
	
    public boolean hasAccount(Personne personne) {
        return utilisateurRepository.existsByPersonne(personne);
    }
	
	public void deletePersonne(Long id) {
		Personne Personne = getPersonneById(id);
		personneRepository.delete(Personne);
	}
}
