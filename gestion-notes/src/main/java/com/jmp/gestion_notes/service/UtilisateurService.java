package com.jmp.gestion_notes.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.jmp.gestion_notes.exception.ResourceNotFoundException;
import com.jmp.gestion_notes.model.Personne;
import com.jmp.gestion_notes.model.Utilisateur;
import com.jmp.gestion_notes.repo.UtilisateurRepository;
import com.jmp.gestion_notes.repo.PersonneRepository;
import java.util.Random;

import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Service
public class UtilisateurService {
	
	private final UtilisateurRepository utilisateurRepository;
	private final PersonneRepository personneRepository;
	private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
	

	@Autowired
	public UtilisateurService(UtilisateurRepository utilisateurRepository, PersonneRepository personneRepository) {
		this.utilisateurRepository = utilisateurRepository;
		this.personneRepository = personneRepository;
	}
	
	// create Utilisateur
	public Map<String, String> createUtilisateur(Utilisateur utilisateur, Long id) {
		Personne personne = personneRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Personne", "id", id));
		String password = generatePassword();
		String login = utilisateur.getPersonne().getNom() + utilisateur.getPersonne().getPrenom();
		utilisateur.setLogin(login);
		utilisateur.setPassword(encoder.encode(password));
		utilisateurRepository.save(utilisateur);
        Map<String, String> response = new HashMap<>();
        utilisateur.setPersonne(personne);
        response.put("login", login);
        response.put("password", password);
		return response;
	}
	
	public List<Utilisateur> getAllUtilisateurs(){
		return utilisateurRepository.findAll();
	}
	
	public Utilisateur updateUtilisateur(Utilisateur updateUtilisateur, Long id) {
		Utilisateur Utilisateur = getUtilisateurById(id);
		
		Utilisateur.setRole(updateUtilisateur.getRole());
		Utilisateur.setLogin(updateUtilisateur.getLogin());
//		todo: hashed password and generate password
//		Utilisateur.setPassword(updateUtilisateur.getPassword());

		
		return utilisateurRepository.save(Utilisateur);
	}
	
	public Utilisateur getUtilisateurById(long id) {
		return utilisateurRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Utilisateur", "id", id));
	}
	
	public void deleteUtilisateur(Long id) {
		Utilisateur Utilisateur = getUtilisateurById(id);
		utilisateurRepository.delete(Utilisateur);
	}
	
	// generate a random password of length 6
    public String generatePassword() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder password = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < 6; i++) {
            int index = random.nextInt(chars.length());
            password.append(chars.charAt(index));
        }
        
        return password.toString();
    }
	
    // initialize password of specific user
    public String initPass(Long id) {
    	Utilisateur user = getUtilisateurById(id);
    	String pw = generatePassword();
    	user.setPassword(encoder.encode(pw));
    	return pw;
    }
	
	
	
	
	
}
