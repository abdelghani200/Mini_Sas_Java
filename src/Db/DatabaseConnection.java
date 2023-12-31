package Db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static  final String DB_URL = "jdbc:postgresql://localhost:5432/biblio_mini_sas";
    private static  final String DB_USER = "postgres";
    private static  final String DB_PASSWORD = "youcode2023";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    public static void closeConnection(Connection connection) throws SQLException {
        if (connection != null){
            connection.close();
        }
    }

}
