package test;

import database.core.GenericDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
        dao = new GenericDAO<Object>();
        dao.connection = connection;
        
        // Initialisation de la table pour les tests
        connection.createStatement().executeUpdate("CREATE TABLE test (id INTEGER PRIMARY KEY, field1 TEXT, field2 INTEGER)");
        connection.createStatement().executeUpdate("INSERT INTO test (id, field1, field2) VALUES (1, 'original', 100)");
        dao.tableName = "test";
    }

    @Test
    public void testUpdateFields() throws SQLException {
        Map<String, Object> fieldsToUpdate = new HashMap<>();
        fieldsToUpdate.put("field1", "updated");
        fieldsToUpdate.put("field2", 200);

        dao.updateFields(1, fieldsToUpdate);

        var resultSet = connection.createStatement().executeQuery("SELECT field1, field2 FROM test WHERE id = 1");
        assertTrue(resultSet.next());
        assertEquals("updated", resultSet.getString("field1"));
        assertEquals(200, resultSet.getInt("field2"));
    }

    // Ajout d'autres tests potentiels...
}