package Implementation;

import Dto.Emprunteur;
import Interfaces.IEmprunteur;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.*;

public class ImEmprunteur implements IEmprunteur {


    @Override
    public Emprunteur insertEmprunteur(Connection connection, Emprunteur emprunteur){
        String insertQuery = "insert into bibliotheque.emprunteurs (nom, prenom, cne) values (?,?,?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
            preparedStatement.setString(1, emprunteur.getNom());
            preparedStatement.setString(2, emprunteur.getPrenom());
            preparedStatement.setString(3, emprunteur.getCne());

            int rowsInserted = preparedStatement.executeUpdate();
            if (rowsInserted > 0) {
                return emprunteur; // Retourne l'emprunteur inséré
            } else {
                return null; // Échec de l'ajout
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Emprunteur> displayEmpruntLivres(Connection connection) {
        String displayQuery = "select * from bibliotheque.emprunteurs";
        List<Emprunteur> emprunteurList = new ArrayList<>();

        try (PreparedStatement preparedStatement = connection.prepareStatement(displayQuery);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                String nom = resultSet.getString("nom");
                String prenom = resultSet.getString("prenom");
                String cne = resultSet.getString("cne");

                Emprunteur emprunteur = new Emprunteur(nom, prenom, cne);
                System.out.println(emprunteur.toString());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return emprunteurList;
    }

    @Override
    public void displayEmprunteurs(Connection connection)
    {
        String displayQuery = "SELECT * FROM bibliotheque.emprunteurs";

        try(PreparedStatement preparedStatement = connection.prepareStatement(displayQuery);
            ResultSet resultSet = preparedStatement.executeQuery())
        {
            while(resultSet.next()) {
                String nom = resultSet.getString("nom");
                String prenom = resultSet.getString("prenom");
                String cne = resultSet.getString("cne");


                Emprunteur emprunteur = new Emprunteur(nom, prenom, cne);

                System.out.println(emprunteur.toString());
            }
        }catch (SQLException e) {
            throw new RuntimeException("Erreur!", e);
        }
    }

    @Override
    public Emprunteur deleteEmprunteur(Connection connection, String cne) {
        String deleteQuery = "delete from bibliotheque.emprunteurs where cne = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery)) {
            preparedStatement.setString(1, cne);

            int rowsDeleted = preparedStatement.executeUpdate();

            if (rowsDeleted > 0) {
                return new Emprunteur("", "", cne); // Retourne l'emprunteur supprimé (avec des champs vides)
            } else {
                return null; // Aucun enregistrement trouvé
            }
        }catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la suppression du Emprunteur", e);
        }
    }

    @Override
    public Emprunteur updateEmprunteur(Connection connection, String cne, String newNom, String newPrenom) {
        String updateQuery = "UPDATE bibliotheque.emprunteurs SET nom = ?, prenom = ? WHERE cne = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
            preparedStatement.setString(1, newNom);
            preparedStatement.setString(2, newPrenom);
            preparedStatement.setString(3, cne);

            int rowsUpdated = preparedStatement.executeUpdate();

            if (rowsUpdated > 0) {
                return new Emprunteur(cne, newNom, newPrenom); // Retourne l'emprunteur mis à jour
            } else {
                return null; // Aucun enregistrement trouvé
            }
        }catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la updateEmprunteur ", e);
        }
    }

    @Override
    public boolean emprunteurExiste(Connection connection, Emprunteur emprunteur) {
        String query = "SELECT COUNT(*) FROM bibliotheque.emprunteurs WHERE cne = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, emprunteur.getCne());

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                resultSet.next();
                int count = resultSet.getInt(1);
                return count > 0;
            } catch (SQLException e) {
                throw new RuntimeException("Erreur lors de la recherche de l'emprunteur", e);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Map<String, Integer> getEmprunteursStatistics(Connection connection) {

        Map<String, Integer> statistics = new HashMap<>();

        String countTotalQuery = "select count(*) from bibliotheque.emprunteurs";

        try(PreparedStatement preparedStatement = connection.prepareStatement(countTotalQuery);
            ResultSet resultSet = preparedStatement.executeQuery()) {
            if (resultSet.next()) {
                int totalEmprunteurs = resultSet.getInt(1);
                statistics.put("TotalEmprunteus", totalEmprunteurs);
            }
        }catch (SQLException e){
            throw new RuntimeException("Erreur lors de la récupération des statistiques", e);
        }

        return statistics;
    }



}
