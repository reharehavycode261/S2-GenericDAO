package test;

import database.core.Affectation;
import database.core.GenericDAO;
import database.core.DBConnection;

import java.sql.Connection;
import java.util.Arrays;

public class Main {

    public static void main(String[] args) {
        // Configuration de la connexion (en supposant que vous avez une méthode utilitaire pour obtenir une connexion)
        Connection connection = null; // récupérez la connexion d'une manière ou d'une autre
        DBConnection dbConnection = new DBConnection(null, connection);

        GenericDAO dao = new GenericDAO();
        dao.connection = connection;  // Assurez-vous que la connexion est définie

        // Test de updateFields
        try {
            Affectation nameUpdate = new Affectation("name", "New Name");
            Affectation ageUpdate = new Affectation("age", 30);

            dao.updateFields("my_table", "123", Arrays.asList(nameUpdate, ageUpdate));

            System.out.println("Update success.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}