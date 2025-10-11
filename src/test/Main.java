package test;

import database.core.Database;
import database.provider.Oracle;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        try {
            // Configuration de la base de données
            Database database = new Oracle("jdbc:oracle:thin:@localhost:1521:xe", "username", "password");
            
            // Test de la méthode count() sur la table Emp
            EmpDAO empDAO = new EmpDAO(database);
            
            // Test du count total
            long totalEmps = empDAO.count();
            System.out.println("Nombre total d'employés: " + totalEmps);
            
            // Test du count avec condition
            long empsWithCondition = empDAO.count("nom LIKE 'D%'");
            System.out.println("Nombre d'employés dont le nom commence par 'D': " + empsWithCondition);
            
        } catch (SQLException e) {
            System.err.println("Erreur SQL: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Erreur: " + e.getMessage());
            e.printStackTrace();
        }
    }
}