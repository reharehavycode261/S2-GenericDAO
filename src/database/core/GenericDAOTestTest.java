package database.core;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class GenericDAOTest {
    private Connection connection;
    private GenericDAO<Object> genericDAO;

    @BeforeEach
    public void setUp() throws Exception {
        // Connexion à une base de données en mémoire pour les tests
        connection = DriverManager.getConnection("jdbc:h2:mem:testdb");
        connection.createStatement().execute("CREATE TABLE test (id INT PRIMARY KEY, name VARCHAR(255), age INT)");
        connection.createStatement().execute("INSERT INTO test (id, name, age) VALUES (1, 'Alice', 30)");

        genericDAO = new GenericDAO<>(connection, "test");
    }

    @Test
    public void testUpdateFields() throws SQLException {
        Map<String, Object> fieldsToUpdate = new HashMap<>();
        fieldsToUpdate.put("name", "Bob");
        fieldsToUpdate.put("age", 35);

        boolean updated = genericDAO.updateFields(1, fieldsToUpdate);
        assertTrue(updated, "The update should return true indicating success");

        try (Statement stmt = connection.createStatement();
             ResultSet resultSet = stmt.executeQuery("SELECT * FROM test WHERE id = 1")) {
            assertTrue(resultSet.next(), "ResultSet should have a row for id 1");
            assertEquals("Bob", resultSet.getString("name"), "Name should be updated to 'Bob'");
            assertEquals(35, resultSet.getInt("age"), "Age should be updated to 35");
        }
    }

    @Test
    public void testUpdateFieldsNonExistentId() throws SQLException {
        Map<String, Object> fieldsToUpdate = new HashMap<>();
        fieldsToUpdate.put("name", "Charlie");
        fieldsToUpdate.put("age", 40);

        boolean updated = genericDAO.updateFields(99, fieldsToUpdate);
        assertFalse(updated, "The update should return false for non-existent id");

        try (Statement stmt = connection.createStatement();
             ResultSet resultSet = stmt.executeQuery("SELECT * FROM test WHERE id = 99")) {
            assertFalse(resultSet.next(), "ResultSet should not have a row for non-existent id");
        }
    }

    @Test
    public void testUpdateFieldsWithInvalidField() {
        Map<String, Object> fieldsToUpdate = new HashMap<>();
        fieldsToUpdate.put("invalid_field", "value");

        SQLException exception = assertThrows(SQLException.class, () -> {
            genericDAO.updateFields(1, fieldsToUpdate);
        }, "Updating with an invalid field should throw SQLException");

        assertNotNull(exception.getMessage(), "Exception message should not be null");
    }

    @Test
    public void testUpdateFieldsWithNullValues() throws SQLException {
        Map<String, Object> fieldsToUpdate = new HashMap<>();
        fieldsToUpdate.put("name", null);
        fieldsToUpdate.put("age", null);

        boolean updated = genericDAO.updateFields(1, fieldsToUpdate);
        assertTrue(updated, "The update should return true even if fields are set to null");

        try (Statement stmt = connection.createStatement();
             ResultSet resultSet = stmt.executeQuery("SELECT * FROM test WHERE id = 1")) {
            assertTrue(resultSet.next(), "ResultSet should have a row for id 1");
            assertNull(resultSet.getString("name"), "Name should be updated to null");
            assertEquals(0, resultSet.getInt("age"), "Age should be updated to null (interpreted as 0)");
        }
    }
}