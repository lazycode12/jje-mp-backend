package com.jmp.gestion_notes.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jmp.gestion_notes.exception.ResourceNotFoundException;
import com.jmp.gestion_notes.model.Semester;
import com.jmp.gestion_notes.repo.SemesterRepository;

@Service
public class SemesterService {

	private final SemesterRepository semesterRepository;
	
	@Autowired
	public SemesterService(SemesterRepository semesterRepository) {
		this.semesterRepository = semesterRepository;
	}
	
	// create Semester
	public Semester addSemester(Semester semester) {
		return semesterRepository.save(semester);
	}
	
	public List<Semester> getAllSemesters(){
		return semesterRepository.findAll();
	}
	
	public Semester updateSemester(Semester updatesemester, Long id) {
		Semester semester = getSemesterById(id);
		
		semester.setSemester(updatesemester.getSemester());
		semester.setSession(updatesemester.getSession());
		semester.setanneEtude(updatesemester.getanneEtude());
		
		return semesterRepository.save(semester);
	}
	
	public Semester getSemesterById(long id) {
		return semesterRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("semester", "id", id));
	}
	
    public Semester getSemesterByDetails(String session, String semester, String anne_etude) {
        return semesterRepository.findBySessionAndSemesterAndAnneEtude(session, semester, anne_etude)
                                 .orElse(null);
    }
	
	public void deleteSemester(Long id) {
		Semester s = getSemesterById(id);
		semesterRepository.delete(s);
	}
}
