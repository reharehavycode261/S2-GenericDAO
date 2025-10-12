package test;

import database.core.DBConnection;
import database.core.Database;
import database.core.GenericDAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        try {
            // Configurer la connexion à la base de données
            Connection connection = DriverManager.getConnection("jdbc:your_database_url");
            Database database = new Database(connection);

            // Exemple d'utilisation de GenericDAO
            GenericDAO myDAO = new GenericDAO();
            myDAO.tableName = "your_table_name";  // Remplacer par le nom de votre table
            myDAO.connection = connection;

            // Comptage sans conditions
            long totalRecords = myDAO.count();
            System.out.println("Total records: " + totalRecords);

            // Comptage avec une condition
            long filteredRecords = myDAO.count("your_column_name = 'some_value'");
            System.out.println("Filtered records: " + filteredRecords);

            // Fermer la connexion
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}