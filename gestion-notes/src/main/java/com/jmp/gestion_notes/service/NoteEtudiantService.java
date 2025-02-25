package com.jmp.gestion_notes.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jmp.gestion_notes.exception.ResourceNotFoundException;
import com.jmp.gestion_notes.model.NoteEtudiant;
import com.jmp.gestion_notes.model.Etudiant;
import com.jmp.gestion_notes.model.Module;
import com.jmp.gestion_notes.repo.NoteEtudiantRepository;


@Service
public class NoteEtudiantService {
	private final NoteEtudiantRepository noteEtudiantRepository;
	private final EtudiantService etudiantService;
	private final ModuleService moduleService;
	
	@Autowired
	public NoteEtudiantService(NoteEtudiantRepository noteEtudiantRepository, EtudiantService etudiantService, ModuleService moduleService) {
		this.noteEtudiantRepository = noteEtudiantRepository;
		this.etudiantService = etudiantService;
		this.moduleService = moduleService;
		
	}
	
	// create NoteEtudiant
	public NoteEtudiant addNoteEtudiant(NoteEtudiant noteetudiant, Long id_etudiant, Long id_module) {
		
		Etudiant etudiant = etudiantService.getEtudiantById(id_etudiant);
		Module module = moduleService.getModuleById(id_module);
		
		noteetudiant.setEtudiant(etudiant);
		noteetudiant.setModule(module);
		
		return noteEtudiantRepository.save(noteetudiant);
	}
	
	public List<NoteEtudiant> getAllNoteEtudiants(){
		return noteEtudiantRepository.findAll();
	}
	
	public NoteEtudiant updateNoteEtudiant(NoteEtudiant updateNoteEtudiant, Long id) {
		NoteEtudiant noteetudiant = getNoteEtudiantById(id);
		
		noteetudiant.setAnneEtude(updateNoteEtudiant.getAnneEtude());
		noteetudiant.setNote(updateNoteEtudiant.getNote());
		
		return noteEtudiantRepository.save(noteetudiant);
	}
	
	public NoteEtudiant getNoteEtudiantById(long id) {
		return noteEtudiantRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("notetudiant", "id", id));
	}
	
	public NoteEtudiant getSingleNote(String cne, Long id_module, String anneEtude) {
		Long id = etudiantService.getEtudiantByCne(cne).get().getId();
		return noteEtudiantRepository.findByEtudiantIdAndModuleIdAndAnneEtude(id, id_module, anneEtude);
	}
	
	public void deleteNoteEtudiant(Long id) {
		NoteEtudiant e = getNoteEtudiantById(id);
		noteEtudiantRepository.delete(e);
	}
	
	
}
