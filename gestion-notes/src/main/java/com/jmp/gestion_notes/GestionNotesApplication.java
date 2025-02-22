package com.jmp.gestion_notes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.jmp.gestion_notes.model.Filiere;
import com.jmp.gestion_notes.model.Niveau;
import com.jmp.gestion_notes.repo.FiliereRepository;
import com.jmp.gestion_notes.repo.NiveauRepository;

@SpringBootApplication
public class GestionNotesApplication {


    public static void main(String[] args) {
        SpringApplication.run(GestionNotesApplication.class, args);
    }


}
