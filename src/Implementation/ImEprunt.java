package Implementation;

import Dto.Emprunt;
import Dto.Emprunteur;
import Dto.Livre;
import Interfaces.IEmprunt;

import java.sql.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ImEprunt implements IEmprunt {

    @Override
    public Emprunt emprunterLivre(Connection connection, Emprunteur emprunteur, Livre livre, Date dateRetourEmprunt, Date dateRetourFixed) {

        String sql = "INSERT INTO bibliotheque.emprunt(cne_emprunteur, ibsn_livre, date_emprunte, date_retour_emprunte, date_retour_fixed) VALUES (?, ?, ?, ?, ?)";

        // Obtenir la date courante
        LocalDate dateEmprunt = LocalDate.now();

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, emprunteur.getCne());
            preparedStatement.setString(2, livre.getIbsn());
            preparedStatement.setDate(3, Date.valueOf(dateEmprunt));
            preparedStatement.setDate(4, dateRetourEmprunt);
            preparedStatement.setDate(5, dateRetourFixed);

            int rowsInserted = preparedStatement.executeUpdate();

            if (rowsInserted > 0) {
                // Créez et retournez un objet Emprunt avec les informations appropriées
                Emprunt emprunt = new Emprunt(emprunteur, livre, Date.valueOf(dateEmprunt), dateRetourEmprunt, dateRetourFixed);
                return emprunt;
            } else {
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Emprunt retournerLivre(Connection connection, Emprunteur emprunteur, Livre livre) {
        String sql = "UPDATE bibliotheque.emprunt SET date_retour_emprunte = ? WHERE cne_emprunteur = ? AND ibsn_livre = ?";

        LocalDate dateRetour = LocalDate.now();

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setDate(1, Date.valueOf(dateRetour));
            preparedStatement.setString(2, emprunteur.getCne());
            preparedStatement.setString(3, livre.getIbsn());

            int rowsUpdated = preparedStatement.executeUpdate();

            if (rowsUpdated > 0) {
                Emprunt emprunt = new Emprunt(emprunteur, livre, null, Date.valueOf(dateRetour), null);
                return emprunt;
            } else {
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
