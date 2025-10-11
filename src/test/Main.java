package test;

import database.core.Database;
import database.core.GenericDAO;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Main {
    
    // Classe de test pour démontrer l'utilisation de count()
    static class TestEntity {
        private int id;
        private String name;
        
        public TestEntity(int id, String name) {
            this.id = id;
            this.name = name;
        }
    }
    
    static class TestDAO extends GenericDAO<TestEntity> {
        public TestDAO(Database database) {
            super(database, "test_table");
        }
        
        @Override
        protected TestEntity createEntity(ResultSet rs) throws SQLException {
            return new TestEntity(
                rs.getInt("id"),
                rs.getString("name")
            );
        }
    }
    
    public static void main(String[] args) {
        try {
            // Initialiser la base de données (à adapter selon votre configuration)
            Database db = new Database("jdbc:postgresql://localhost:5432/testdb", "user", "password");
            
            TestDAO dao = new TestDAO(db);
            
            // Test 1: Compter les enregistrements
            long count = dao.count();
            System.out.println("Nombre d'enregistrements: " + count);
            
            // Test 2: Vérifier la cohérence avec findAll()
            int allRecords = dao.findAll().size();
            System.out.println("Nombre d'enregistrements via findAll(): " + allRecords);
            
            assert count == allRecords : "Le compte devrait être égal au nombre d'enregistrements retournés par findAll()";
            
            System.out.println("Tous les tests ont réussi!");
            
        } catch (SQLException e) {
            System.err.println("Erreur lors des tests: " + e.getMessage());
            e.printStackTrace();
        }
    }
}