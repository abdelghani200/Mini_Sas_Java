package Interfaces;

import Dto.Emprunt;
import Dto.Emprunteur;
import Dto.Livre;

import java.sql.*;
import java.util.List;


public interface IEmprunt {

    Emprunt emprunterLivre(Connection connection, Emprunteur emprunteur, Livre livre, Date dateRetourEmprunt, Date dateRetourFixed);

    Emprunt retournerLivre(Connection connection, Emprunteur emprunteur, Livre isbnLivre);
}
