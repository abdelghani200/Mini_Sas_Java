package Interfaces;

import Dto.Emprunteur;

import java.sql.Connection;
import java.util.*;


public interface IEmprunteur {

    Emprunteur insertEmprunteur(Connection connection, Emprunteur emprunteur);
    List<Emprunteur> displayEmpruntLivres(Connection connection);
    void displayEmprunteurs(Connection connection);
    Emprunteur deleteEmprunteur(Connection connection, String cne);
    Emprunteur updateEmprunteur(Connection connection, String cne, String newNom, String newPrenom);
    boolean emprunteurExiste(Connection connection, Emprunteur emprunteur);
    Map<String, Integer> getEmprunteursStatistics(Connection connection);


}
