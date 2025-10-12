package test;

import database.core.*;
import java.sql.SQLException;

public class Main {

    public static void main(String[] args) {
        try {
            // Supposons que DBConnection soit correctement configuré pour se connecter à la base de données.
            DBConnection connection = new DBConnection();
            GenericDAO<Plat> platDAO = new GenericDAO<>(connection, "Plat");

            // Test pour vérifier la méthode count
            long nombreEnregistrements = platDAO.count();
            System.out.println("Nombre total d'enregistrements dans 'Plat': " + nombreEnregistrements);

        } catch (SQLException e) {
            System.err.println("Erreur lors de l'exécution de la méthode count(): " + e.getMessage());
        }
    }
}