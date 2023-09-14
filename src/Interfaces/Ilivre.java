package Interfaces;

import Dto.Livre;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

public interface Ilivre {


    Livre insertLivre(Livre l, Connection connection);
    List<Livre> displayLivres(Connection connection);
    void updateLivre(Connection connection, String ibsn, String newTitre, String newAuteur);
    Livre deleteLivre(String ibsn, Connection connection);
    Livre rechercherLivre(Connection connection, String recherchePar, String valeurRecherche);
    boolean livreExiste(Connection connection, String ibsn) throws SQLException;
    Map<String, Integer> getLivresStatistics(Connection connection);
    void changeStatutLivrePerdu(Connection connection, String ibsn);
    int nombre_jour(Connection connection, String ibsn);

}
