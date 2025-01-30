package com.jmp.gestion_notes.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jmp.gestion_notes.model.Semester;

public interface SemesterRepository extends JpaRepository<Semester, Long> {
	Optional<Semester> findBySessionAndSemesterAndAnneEtude(String session, String semester, String anne_etude);
}
