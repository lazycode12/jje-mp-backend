package com.jmp.gestion_notes.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jmp.gestion_notes.model.Note;

public interface NoteRepository extends JpaRepository<Note, Long> {

}
