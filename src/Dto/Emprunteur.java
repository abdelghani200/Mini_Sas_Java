package Dto;

import java.util.List;

public class Emprunteur {

    private String cne;
    private String nom;
    private String prenom;

    List<Emprunt> livres;


    public Emprunteur(String nom, String prenom, String cne) {
        this.nom = nom;
        this.prenom = prenom;
        this.cne = cne;
        this.livres = livres;
    }

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

    @Override
    public String toString() {
        return "Emprunteur{" +
                "cne='" + cne + '\'' +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                '}';
    }
}
