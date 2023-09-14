package Implementation;

import Dto.Emprunteur;
import Dto.Livre;
import Interfaces.Ilivre;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class ImpLivre implements Ilivre {

    @Override
    public Livre insertLivre(Livre l, Connection connection) {
            String insertQuery = "INSERT INTO bibliotheque.livres (titre, auteur, ibsn, statut) VALUES (?, ?, ?, ?::StatutLivre)";

            try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
                preparedStatement.setString(1, l.getTitre());
                preparedStatement.setString(2, l.getAuteur());
                preparedStatement.setString(3, l.getIbsn());
                preparedStatement.setString(4, l.getStatut().toString());

                int rowsInserted = preparedStatement.executeUpdate();
                if (rowsInserted > 0) {
                    return l;
                } else {
                    return null;
                }
            } catch (SQLException e) {
                System.err.println("Erreur lors de l'insertion du livre : " + e.getMessage());
                return null;
            }

    }
    //dto ,est de faciliter le transfert de données entre différentes parties d'une application informatique
    //dao  est un modèle de conception couramment utilisé en programmation pour isoler le code métier de l'accès direct à la base de données.
    @Override
    public List<Livre> displayLivres(Connection connection) {
        String displayQuery = "SELECT * FROM bibliotheque.livres";
        List<Livre> livreList = new ArrayList<>(); // Créez une liste de livres pour stocker les résultats

        try (PreparedStatement preparedStatement = connection.prepareStatement(displayQuery);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                String titre = resultSet.getString("titre");
                String auteur = resultSet.getString("auteur");
                String ibsn = resultSet.getString("ibsn");
                String statutStr = resultSet.getString("statut");

                Livre l = new Livre(titre, auteur, ibsn, Livre.StatutLivre.valueOf(statutStr));
                System.out.println(l.toString());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return livreList;
    }

    @Override
    public void updateLivre(Connection connection, String ibsn, String newTitre, String newAuteur) {
        String updateQuery = "UPDATE bibliotheque.livres SET titre = ?, auteur = ? WHERE ibsn = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
            preparedStatement.setString(1, newTitre);
            preparedStatement.setString(2, newAuteur);
            preparedStatement.setString(3, ibsn);

            int rowsUpdated = preparedStatement.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("Livre mis à jour avec succès.");
            } else {
                System.out.println("Aucun livre trouvé avec cet IBSN.");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Livre deleteLivre(String ibsn, Connection connection) {
        String selectQuery = "SELECT statut FROM bibliotheque.livres WHERE ibsn = ?";
        String deleteQuery = "DELETE FROM bibliotheque.livres WHERE ibsn = ?";
        try (PreparedStatement selectStatement = connection.prepareStatement(selectQuery);
             PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery)) {
            // Récupérez le statut actuel du livre
            selectStatement.setString(1, ibsn);
            ResultSet resultSet = selectStatement.executeQuery();

            if (resultSet.next()) {
                String statut = resultSet.getString("statut");

                // Vérifiez le statut et effectuez la suppression appropriée
                if ("DISPONIBLE".equals(statut)) {
                    deleteStatement.setString(1, ibsn);
                    int rowsDeleted = deleteStatement.executeUpdate();

                    if (rowsDeleted > 0) {
                        return new Livre("", "", ibsn, Livre.StatutLivre.DISPONIBLE);
                    }
                } else if ("EMPRUNTE".equals(statut) || "PERDU".equals(statut)) {
                    return null;
                }
            }

            return null;
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la suppression du livre", e);
        }
    }

    @Override
    public Livre rechercherLivre(Connection connection, String rechercherPar, String valeurRecherche) {
        String findQuery = "";

        if ("ibsn".equalsIgnoreCase(rechercherPar)) {
            findQuery = "SELECT * FROM bibliotheque.livres WHERE ibsn = ?";
        } else if ("auteur".equalsIgnoreCase(rechercherPar)) {
            findQuery = "SELECT * FROM bibliotheque.livres WHERE auteur = ?";
        } else {
            throw new IllegalArgumentException("Type de recherche invalide. Utilisez 'ibsn' ou 'auteur'.");
        }

        try (PreparedStatement preparedStatement = connection.prepareStatement(findQuery)) {
            preparedStatement.setString(1, valeurRecherche);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    String titre = resultSet.getString("titre");
                    String auteur = resultSet.getString("auteur");

                    return new Livre(titre, auteur, resultSet.getString("ibsn"), Livre.StatutLivre.DISPONIBLE);
                } else {
                    return null;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean livreExiste(Connection connection, String ibsn) {
        String query = "SELECT COUNT(*) FROM bibliotheque.livres WHERE ibsn = ? AND statut NOT IN ('EMPRUNTE', 'PERDU')";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, ibsn);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                resultSet.next();
                int count = resultSet.getInt(1);
                return count > 0;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Map<String, Integer> getLivresStatistics(Connection connection) {
        Map<String, Integer> statistics = new HashMap<>();

        // Statistique : Nombre total de livres
        String countTotalQuery = "SELECT COUNT(*) FROM bibliotheque.livres";
        String countEmpruntesQuery = "SELECT COUNT(*) FROM bibliotheque.livres WHERE statut = 'EMPRUNTE'";
        String countDisponiblesQuery = "SELECT COUNT(*) FROM bibliotheque.livres WHERE statut = 'DISPONIBLE'";

        String countEmprunteursQuery = "SELECT COUNT(DISTINCT cne_emprunteur) FROM bibliotheque.emprunt WHERE ibsn_livre IN (SELECT ibsn FROM bibliotheque.livres WHERE statut = 'EMPRUNTE')";

        try (PreparedStatement totalPreparedStatement = connection.prepareStatement(countTotalQuery);
             PreparedStatement empruntesPreparedStatement = connection.prepareStatement(countEmpruntesQuery);
             PreparedStatement disponiblesPreparedStatement = connection.prepareStatement(countDisponiblesQuery);
             PreparedStatement livresEmpruntPreparedStatement = connection.prepareStatement(countEmprunteursQuery);
             ResultSet totalResultSet = totalPreparedStatement.executeQuery();
             ResultSet empruntesResultSet = empruntesPreparedStatement.executeQuery();
             ResultSet livresEmpruntResultSet = livresEmpruntPreparedStatement.executeQuery();
             ResultSet disponiblesResultSet = disponiblesPreparedStatement.executeQuery()) {

            if (totalResultSet.next()) {
                int totalLivres = totalResultSet.getInt(1);
                statistics.put("TotalLivres", totalLivres);
            }

            if (empruntesResultSet.next()) {
                int livresEmpruntes = empruntesResultSet.getInt(1);
                statistics.put("LivresEmpruntes", livresEmpruntes);
            }

            if (disponiblesResultSet.next()) {
                int livresDisponibles = disponiblesResultSet.getInt(1);
                statistics.put("LivresDisponibles", livresDisponibles);
            }

            if (livresEmpruntResultSet.next()) {
                int EmpLivres = livresEmpruntResultSet.getInt(1);
                statistics.put("EmprunteurEmprunteLivres", EmpLivres);
            }
        }
        catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération des statistiques", e);
        }

        return statistics;
    }

    @Override
    public void changeStatutLivrePerdu(Connection connection, String ibsn) {
        String updateQuery = "UPDATE bibliotheque.livres SET statut = ?::StatutLivre WHERE ibsn = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
            preparedStatement.setString(1, Livre.StatutLivre.PERDU.toString()); // Définissez le statut sur "PERDU"
            preparedStatement.setString(2, ibsn);

            int rowsUpdated = preparedStatement.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("Statut du livre mis à jour avec succès à 'PERDU'.");
            } else {
                System.out.println("Aucun livre trouvé avec cet IBSN.");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

@Override
public int nombre_jour(Connection connection, String ibsn) {
    String sql = "SELECT date_emprunte, date_retour_fixed FROM bibliotheque.emprunt WHERE ibsn_livre = ?";

    try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
        preparedStatement.setString(1, ibsn);

        ResultSet resultSet = preparedStatement.executeQuery();

        if (resultSet.next()) {
            java.util.Date dateEmprunt = resultSet.getDate("date_emprunte");
            java.util.Date dateRetour = resultSet.getDate("date_retour_fixed");

            // Vérifiez si les deux dates ne sont pas nulles
            if (dateEmprunt != null && dateRetour != null) {
                // Calculer la différence en millisecondes entre les deux dates
                long differenceEnMillisecondes = dateRetour.getTime() - dateEmprunt.getTime();

                // Convertir la différence en jours
                int nombreDeJours = (int) (differenceEnMillisecondes / (1000 * 60 * 60 * 24));

                return nombreDeJours;
            } else {
                System.out.println("Les dates de l'emprunt ou du retour sont nulles.");
            }
        } else {
            System.out.println("Aucun enregistrement trouvé avec l'IBSN spécifié.");
        }
    } catch (SQLException e) {
        System.err.println("Erreur SQL : " + e.getMessage());
    }

    return -1; // Vous pouvez retourner une valeur par défaut en cas d'erreur.
}




}
