package com.jmp.gestion_notes.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jmp.gestion_notes.model.Semester;
import com.jmp.gestion_notes.service.SemesterService;

@RestController
@RequestMapping("/semestres")
public class SemesterController {
	@Autowired
	private SemesterService semesterService;
	
	// endpoint to get all Semesters
    @GetMapping("")
    public ResponseEntity<List<Semester>> getAllUsers() {
        List<Semester> Semesters = semesterService.getAllSemesters();
        return new ResponseEntity<>(Semesters, HttpStatus.OK);
    }
    
    // endpoint to get a specific Semester by id
    @GetMapping("/{id}")
    public ResponseEntity<Semester> getSemesterById(@PathVariable Long id){
    	Semester Semester = semesterService.getSemesterById(id);
    	return new ResponseEntity<>(Semester, HttpStatus.OK);
    }
    
    @PostMapping("")
    public ResponseEntity<Semester> createSemester(@RequestBody Semester Semester){
    	Semester NewSemester = semesterService.addSemester(Semester);
    	return new ResponseEntity<>(NewSemester, HttpStatus.CREATED);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Semester> updateSemester(@PathVariable Long id, @RequestBody Semester Semester){
    	Semester updateSemester = semesterService.updateSemester(Semester, id);
    	return new ResponseEntity<>(updateSemester, HttpStatus.OK);
    }
    
    // Endpoint to delete a user by id
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSemester(@PathVariable Long id) {
    	semesterService.deleteSemester(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
