package test;

import database.core.GenericDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class GenericDAOTest {
    private GenericDAO<Object> dao;
    private Connection connection;

    @BeforeEach
    public void setUp() throws SQLException {
        // Supposons que nous utilisions une base de données de test en mémoire
        connection = DriverManager.getConnection("jdbc:sqlite::memory:");
        dao = new GenericDAO<>();
        dao.connection = connection;
        
        // Initialisation de la table pour les tests
        connection.createStatement().executeUpdate("CREATE TABLE test (id INTEGER PRIMARY KEY, field1 TEXT, field2 INTEGER)");
        connection.createStatement().executeUpdate("INSERT INTO test (id, field1, field2) VALUES (1, 'original', 100)");
        dao.tableName = "test";
    }

    @AfterEach
    public void tearDown() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

    @Test
    public void testUpdateFields() throws SQLException {
        // Test positif : mise à jour des champs existants
        Map<String, Object> fieldsToUpdate = new HashMap<>();
        fieldsToUpdate.put("field1", "updated");
        fieldsToUpdate.put("field2", 200);

        dao.updateFields(1, fieldsToUpdate);

        var resultSet = connection.createStatement().executeQuery("SELECT field1, field2 FROM test WHERE id = 1");
        assertTrue(resultSet.next(), "Le résultat doit contenir une ligne.");
        assertEquals("updated", resultSet.getString("field1"), "Le champ 'field1' doit être mis à jour.");
        assertEquals(200, resultSet.getInt("field2"), "Le champ 'field2' doit être mis à jour.");
    }

    @Test
    public void testUpdateFieldsWithNonExistentId() throws SQLException {
        // Test négatif : mise à jour d'un enregistrement inexistant
        Map<String, Object> fieldsToUpdate = new HashMap<>();
        fieldsToUpdate.put("field1", "updated");
        fieldsToUpdate.put("field2", 200);

        assertThrows(SQLException.class, () -> {
            dao.updateFields(999, fieldsToUpdate);
        }, "Une exception doit être levée pour un ID inexistant.");
    }

    @Test
    public void testUpdateFieldsWithEmptyFields() throws SQLException {
        // Test négatif : mise à jour avec une map vide
        Map<String, Object> fieldsToUpdate = new HashMap<>();

        assertThrows(IllegalArgumentException.class, () -> {
            dao.updateFields(1, fieldsToUpdate);
        }, "Une exception doit être levée pour une mise à jour avec des champs vides.");
    }

    @Test
    public void testUpdateFieldsWithNullFields() {
        // Test négatif : mise à jour avec une map null
        assertThrows(NullPointerException.class, () -> {
            dao.updateFields(1, null);
        }, "Une exception doit être levée pour une mise à jour avec une map null.");
    }
}