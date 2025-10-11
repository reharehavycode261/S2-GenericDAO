package test;

import database.core.Database;
import database.provider.PostgreSQL;

public class Main {
    public static void main(String[] args) {
        try {
            // Configuration de la base de données de test
            Database db = new PostgreSQL("localhost", "5432", "test_db", "user", "password");
            
            // Test de count() avec la classe Plat
            Plat plat = new Plat();
            plat.setDatabase(db);
            
            // Test 1: Compter tous les plats
            long totalPlats = plat.count();
            System.out.println("Nombre total de plats: " + totalPlats);
            
            // Test 2: Ajouter un nouveau plat et recompter
            Plat nouveauPlat = new Plat("Pizza");
            nouveauPlat.setDatabase(db);
            nouveauPlat.save();
            
            long nouveauTotal = plat.count();
            System.out.println("Nouveau nombre total de plats: " + nouveauTotal);
            assert nouveauTotal == totalPlats + 1 : "Le compteur n'a pas été incrémenté correctement";
            
            System.out.println("Tous les tests ont réussi!");
            
        } catch (Exception e) {
            System.err.println("Erreur lors des tests: " + e.getMessage());
            e.printStackTrace();
        }
    }
}