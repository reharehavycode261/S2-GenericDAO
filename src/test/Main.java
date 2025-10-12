package test;

import database.core.GenericDAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        try {
            // Remplacez par vos propres paramètres de connexion
            Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "username", "password");

            // Initialisation du DAO avec une table fictive
            GenericDAO<Emp> empDAO = new GenericDAO<>("EMP", connection);

            // Appel de la méthode count pour obtenir le nombre d'enregistrements
            long count = empDAO.count();
            System.out.println("Nombre d'enregistrements dans la table EMP: " + count);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}