package Dto;

import java.sql.Date;
public class Emprunt {

    private Emprunteur emprunteur;
    private Livre livre;
    private Date date_emprunte;
    private Date date_rotour_emprunte;
    private  Date dateRetourFixed;


    public Emprunt(Emprunteur emprunteur, Livre livre, Date date_emprunte, Date date_rotour_emprunte, Date dateRetourFixed) {
        this.emprunteur = emprunteur;
        this.livre = livre;
        this.date_emprunte = date_emprunte;
        this.date_rotour_emprunte = date_rotour_emprunte;
        this.dateRetourFixed = dateRetourFixed;
    }

    public Emprunteur getEmprunteur() {
        return emprunteur;
    }

    public Date getDateRetourFixed() {
        return dateRetourFixed;
    }

    public void setDateRetourFixed(Date dateRetourFixed) {
        this.dateRetourFixed = dateRetourFixed;
    }

    public void setEmprunteur(Emprunteur emprunteur) {
        this.emprunteur = emprunteur;
    }

    public Livre getLivre() {
        return livre;
    }

    public void setLivre(Livre livre) {
        this.livre = livre;
    }

    public Date getDate_rotour_emprunte() {
        return date_rotour_emprunte;
    }

    public void setDate_rotour_emprunte(Date date_rotour_emprunte) {
        this.date_rotour_emprunte = date_rotour_emprunte;
    }

    public Date getDate_emprunte() {
        return date_emprunte;
    }

    public void setDate_emprunte(Date date_emprunte) {
        this.date_emprunte = date_emprunte;
    }
}
