package com.jmp.gestion_notes.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jmp.gestion_notes.exception.ResourceNotFoundException;

import com.jmp.gestion_notes.model.Note;
import com.jmp.gestion_notes.model.Etudiant;
import com.jmp.gestion_notes.model.Matiere;
import com.jmp.gestion_notes.model.Semester;

import com.jmp.gestion_notes.repo.NoteRepository;
import com.jmp.gestion_notes.repo.EtudiantRepository;
import com.jmp.gestion_notes.repo.SemesterRepository;
import com.jmp.gestion_notes.repo.MatiereRepository;

@Service
public class NoteService {
	private final NoteRepository noteRepository;
	private final EtudiantRepository etudiantRepository;
	private final SemesterRepository semesterRepository;
	private final MatiereRepository matiereRepository;
	
	@Autowired
	public NoteService(
			NoteRepository noteRepository,
			EtudiantRepository etudiantRepository,
			SemesterRepository semesterRepository,
			MatiereRepository matiereRepository) 
	{
		
		this.noteRepository = noteRepository;
		this.etudiantRepository = etudiantRepository;
		this.semesterRepository = semesterRepository;
		this.matiereRepository = matiereRepository;
	}
	
	// create Note
	public Note addNote(Note note, Long id_etudiant, Long id_semester, Long id_matiere) {
		
		Etudiant etudiant = etudiantRepository.findById(id_etudiant).orElseThrow(() -> new ResourceNotFoundException("etudiant", "id", id_etudiant));
		Semester semester = semesterRepository.findById(id_semester).orElseThrow(() -> new ResourceNotFoundException("semester", "id", id_etudiant));
		Matiere matiere = matiereRepository.findById(id_matiere).orElseThrow(() -> new ResourceNotFoundException("matiere", "id", id_etudiant));
		
		note.setEtudiant(etudiant);
		note.setMatiere(matiere);
		note.setSemester(semester);
		
		return noteRepository.save(note);
	}
	
	public List<Note> getAllNotes(){
		return noteRepository.findAll();
	}
	
	public Note updateNote(Note updatenote, Long id) {
		Note note = getNoteById(id);
		
		note.setValeur(updatenote.getValeur());
		
		return noteRepository.save(note);
	}
	
	public Note getNoteById(long id) {
		return noteRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("note", "id", id));
	}
	
	public void deleteNote(Long id) {
		Note n = getNoteById(id);
		noteRepository.delete(n);
	}
	
}
