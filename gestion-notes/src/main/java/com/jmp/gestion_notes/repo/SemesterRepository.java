package com.jmp.gestion_notes.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jmp.gestion_notes.model.Semester;

public interface SemesterRepository extends JpaRepository<Semester, Long> {

}
