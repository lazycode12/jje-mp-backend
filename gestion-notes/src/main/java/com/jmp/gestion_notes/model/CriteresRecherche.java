package com.jmp.gestion_notes.model;

public class CriteresRecherche {
    private String cne;
    private String nom;
    private String prenom;
    private Long idNiveau;
    private Boolean estAjourne;

    
    // Getters and setters
    public String getCne() {
        return cne;
    }

    public void setCne(String cne) {
        this.cne = cne;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public Long getIdNiveau() {
        return idNiveau;
    }

    public void setIdNiveau(Long idNiveau) {
        this.idNiveau = idNiveau;
    }

    public Boolean getEstAjourne() {
        return estAjourne;
    }

    public void setEstAjourne(Boolean estAjourne) {
        this.estAjourne = estAjourne;
    }
}
