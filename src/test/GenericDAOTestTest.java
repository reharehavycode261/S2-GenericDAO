package test;

import database.core.GenericDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class GenericDAOTest {
    private GenericDAO<Object> genericDAO;
    private Connection connection;

    @BeforeEach
    public void setUp() throws SQLException {
        connection = DriverManager.getConnection("jdbc:h2:mem:testdb", "sa", "");
        genericDAO = new GenericDAO<>(connection, "your_table_name");
        // Set up test database schema
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("CREATE TABLE your_table_name (id INT PRIMARY KEY, column1 VARCHAR(255), column2 INT)");
            stmt.execute("INSERT INTO your_table_name (id, column1, column2) VALUES (1, 'initialValue', 0)");
        }
    }

    @AfterEach
    public void tearDown() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            try (Statement stmt = connection.createStatement()) {
                stmt.execute("DROP TABLE your_table_name");
            }
            connection.close();
        }
    }

    @Test
    public void testUpdateFields_withValidFields_shouldNotThrowException() throws SQLException {
        Map<String, Object> fields = new HashMap<>();
        fields.put("column1", "value1");
        fields.put("column2", 123);

        // Assuming that '1' is a valid ID in the test database.
        genericDAO.updateFields(1, fields);

        // Verify the update
        try (Statement stmt = connection.createStatement()) {
            var resultSet = stmt.executeQuery("SELECT column1, column2 FROM your_table_name WHERE id = 1");
            Assertions.assertTrue(resultSet.next(), "Record should exist for ID 1");
            Assertions.assertEquals("value1", resultSet.getString("column1"), "Column1 should be updated to 'value1'");
            Assertions.assertEquals(123, resultSet.getInt("column2"), "Column2 should be updated to 123");
        }
    }

    @Test
    public void testUpdateFields_withEmptyFields_shouldThrowIllegalArgumentException() {
        Map<String, Object> fields = new HashMap<>();

        assertThrows(IllegalArgumentException.class, () -> genericDAO.updateFields(1, fields), "Updating with empty fields should throw IllegalArgumentException");
    }

    @Test
    public void testUpdateFields_withInvalidId_shouldThrowSQLException() {
        Map<String, Object> fields = new HashMap<>();
        fields.put("column1", "value1");

        assertThrows(SQLException.class, () -> genericDAO.updateFields(999, fields), "Updating with an invalid ID should throw SQLException");
    }

    @Test
    public void testUpdateFields_withNullFields_shouldThrowNullPointerException() {
        assertThrows(NullPointerException.class, () -> genericDAO.updateFields(1, null), "Updating with null fields should throw NullPointerException");
    }
}