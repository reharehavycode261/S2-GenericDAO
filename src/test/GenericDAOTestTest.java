package test;

import database.core.DBConnection;
import database.core.GenericDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

class GenericDAOTest {
    private GenericDAO<Object> dao;
    private DBConnection dbConnection;
    private Connection connection;

    @BeforeEach
    void setUp() throws SQLException {
        // Configurer une connexion en mémoire pour les tests
        connection = DriverManager.getConnection("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1");
        dbConnection = new DBConnection(null, connection);

        // Configuration initiale de la base de données
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("CREATE TABLE test_table (id INT PRIMARY KEY, name VARCHAR(255), age INT)");
            stmt.execute("INSERT INTO test_table (id, name, age) VALUES (1, 'Alice', 30)");
        }
        
        dao = new GenericDAO<>(connection);
    }

    @Test
    void testUpdateFields_Success() throws SQLException {
        // Mettre à jour le nom et l'âge de l'utilisateur avec id 1
        dao.updateFields(dbConnection, "test_table", new String[]{"name", "age"}, new Object[]{"Bob", 35}, "id = 1");

        // Vérification des modifications
        try (Statement stmt = connection.createStatement()) {
            var rs = stmt.executeQuery("SELECT name, age FROM test_table WHERE id = 1");
            if (rs.next()) {
                assertEquals("Bob", rs.getString("name"), "Le nom devrait être mis à jour à 'Bob'");
                assertEquals(35, rs.getInt("age"), "L'âge devrait être mis à jour à 35");
            } else {
                fail("Aucune donnée trouvée pour id = 1");
            }
        }
    }

    @Test
    void testUpdateFields_FieldValueMismatch() {
        // Vérifier si une exception est levée quand il y a incohérence entre nombre de champs et valeurs
        assertThrows(IllegalArgumentException.class, () -> {
            dao.updateFields(dbConnection, "test_table", new String[]{"name"}, new Object[]{"Bob", 35}, "id = 1");
        }, "Une IllegalArgumentException devrait être levée si les champs et les valeurs ne correspondent pas en nombre");
    }

    @Test
    void testUpdateFields_InvalidTable() {
        // Vérifier si une exception est levée pour une table invalide
        assertThrows(SQLException.class, () -> {
            dao.updateFields(dbConnection, "invalid_table", new String[]{"name"}, new Object[]{"Bob"}, "id = 1");
        }, "Une SQLException devrait être levée pour une table invalide");
    }

    @Test
    void testUpdateFields_InvalidCondition() throws SQLException {
        // Mettre à jour avec une condition qui ne correspond à aucune ligne
        dao.updateFields(dbConnection, "test_table", new String[]{"name"}, new Object[]{"Charlie"}, "id = 99");

        // Vérification qu'aucune modification n'a été faite
        try (Statement stmt = connection.createStatement()) {
            var rs = stmt.executeQuery("SELECT name FROM test_table WHERE id = 1");
            if (rs.next()) {
                assertEquals("Alice", rs.getString("name"), "Le nom ne devrait pas être modifié pour id = 1");
            } else {
                fail("Aucune donnée trouvée pour id = 1");
            }
        }
    }

    @Test
    void testUpdateFields_NullValues() {
        // Vérifier si une exception est levée quand les valeurs sont nulles
        assertThrows(NullPointerException.class, () -> {
            dao.updateFields(dbConnection, "test_table", new String[]{"name"}, null, "id = 1");
        }, "Une NullPointerException devrait être levée si les valeurs sont nulles");
    }
}