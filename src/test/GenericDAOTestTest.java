package test;

import database.core.GenericDAO;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.function.Executable;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class GenericDAOTest {
    private GenericDAO<Object> genericDAO;
    private Connection connection;

    @BeforeEach
    public void setUp() throws SQLException {
        connection = DriverManager.getConnection("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1", "sa", "");
        genericDAO = new GenericDAO<>();
        genericDAO.connection = connection;
        genericDAO.tableName = "test_table";

        connection.createStatement().execute("CREATE TABLE test_table (id INT PRIMARY KEY, name VARCHAR(255), age INT)");
        connection.createStatement().execute("INSERT INTO test_table (id, name, age) VALUES (1, 'John Doe', 30)");
    }

    @Test
    public void testUpdateFields_successfulUpdate() throws SQLException {
        Map<String, Object> fieldsToUpdate = new HashMap<>();
        fieldsToUpdate.put("name", "Jane Doe");
        fieldsToUpdate.put("age", 31);

        Map<String, Object> conditions = new HashMap<>();
        conditions.put("id", 1);

        genericDAO.updateFields(fieldsToUpdate, conditions);

        var resultSet = connection.createStatement().executeQuery("SELECT name, age FROM test_table WHERE id = 1");
        assertTrue(resultSet.next(), "ResultSet should have a next element");
        assertEquals("Jane Doe", resultSet.getString("name"), "Name should be updated to 'Jane Doe'");
        assertEquals(31, resultSet.getInt("age"), "Age should be updated to 31");
    }

    @Test
    public void testUpdateFields_noMatchingCondition() throws SQLException {
        Map<String, Object> fieldsToUpdate = new HashMap<>();
        fieldsToUpdate.put("name", "Jane Doe");
        fieldsToUpdate.put("age", 31);

        Map<String, Object> conditions = new HashMap<>();
        conditions.put("id", 2); // No record with id 2

        genericDAO.updateFields(fieldsToUpdate, conditions);

        var resultSet = connection.createStatement().executeQuery("SELECT name, age FROM test_table WHERE id = 1");
        assertTrue(resultSet.next(), "ResultSet should have a next element");
        assertEquals("John Doe", resultSet.getString("name"), "Name should remain 'John Doe'");
        assertEquals(30, resultSet.getInt("age"), "Age should remain 30");
    }

    @Test
    public void testUpdateFields_emptyFieldsToUpdate() throws SQLException {
        Map<String, Object> fieldsToUpdate = new HashMap<>(); // Empty fields to update

        Map<String, Object> conditions = new HashMap<>();
        conditions.put("id", 1);

        Executable executable = () -> genericDAO.updateFields(fieldsToUpdate, conditions);
        assertThrows(IllegalArgumentException.class, executable, "Should throw IllegalArgumentException for empty fieldsToUpdate");
    }

    @Test
    public void testUpdateFields_nullFieldsToUpdate() {
        Map<String, Object> conditions = new HashMap<>();
        conditions.put("id", 1);

        Executable executable = () -> genericDAO.updateFields(null, conditions);
        assertThrows(IllegalArgumentException.class, executable, "Should throw IllegalArgumentException for null fieldsToUpdate");
    }

    @Test
    public void testUpdateFields_nullConditions() {
        Map<String, Object> fieldsToUpdate = new HashMap<>();
        fieldsToUpdate.put("name", "Jane Doe");
        fieldsToUpdate.put("age", 31);

        Executable executable = () -> genericDAO.updateFields(fieldsToUpdate, null);
        assertThrows(IllegalArgumentException.class, executable, "Should throw IllegalArgumentException for null conditions");
    }

    @Test
    public void testUpdateFields_sqlException() throws SQLException {
        connection.close(); // Close connection to force SQLException

        Map<String, Object> fieldsToUpdate = new HashMap<>();
        fieldsToUpdate.put("name", "Jane Doe");
        fieldsToUpdate.put("age", 31);

        Map<String, Object> conditions = new HashMap<>();
        conditions.put("id", 1);

        Executable executable = () -> genericDAO.updateFields(fieldsToUpdate, conditions);
        assertThrows(SQLException.class, executable, "Should throw SQLException due to closed connection");
    }
}