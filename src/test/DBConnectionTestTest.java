import database.core.DBConnection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

public class DBConnectionTest {
    
    private DBConnection dbConnection;

    @BeforeEach
    public void setup() throws SQLException {
        // Assurez-vous de remplacer par une connexion de test valide
        Connection connection = DriverManager.getConnection("jdbc:h2:mem:testdb", "sa", "");
        dbConnection = new DBConnection(connection);

        // Set up the database schema for testing - Example
        connection.createStatement().execute("CREATE TABLE test_table (id INT PRIMARY KEY, name VARCHAR(255), age INT)");
        connection.createStatement().execute("INSERT INTO test_table (id, name, age) VALUES (1, 'John Doe', 30)");
    }

    @Test
    public void testUpdateFields() throws SQLException {
        dbConnection.updateFields("test_table", new String[]{"name", "age"}, new Object[]{"Jane Doe", 25}, "id = 1");

        // Check if the update was successful
        var resultSet = dbConnection.connection.createStatement().executeQuery("SELECT name, age FROM test_table WHERE id = 1");
        assertTrue(resultSet.next(), "ResultSet should have at least one row");
        assertEquals("Jane Doe", resultSet.getString("name"), "Name should be updated to 'Jane Doe'");
        assertEquals(25, resultSet.getInt("age"), "Age should be updated to 25");
    }

    @Test
    public void testUpdateFieldsWithInvalidTable() {
        assertThrows(SQLException.class, () -> {
            dbConnection.updateFields("invalid_table", new String[]{"name"}, new Object[]{"Jane Doe"}, "id = 1");
        }, "Updating a non-existent table should throw SQLException");
    }

    @Test
    public void testUpdateFieldsWithInvalidColumn() {
        assertThrows(SQLException.class, () -> {
            dbConnection.updateFields("test_table", new String[]{"invalid_column"}, new Object[]{"Jane Doe"}, "id = 1");
        }, "Updating a non-existent column should throw SQLException");
    }

    @Test
    public void testUpdateFieldsWithInvalidCondition() throws SQLException {
        dbConnection.updateFields("test_table", new String[]{"name", "age"}, new Object[]{"Jane Doe", 25}, "id = 999");

        // Check if the update was unsuccessful
        var resultSet = dbConnection.connection.createStatement().executeQuery("SELECT name, age FROM test_table WHERE id = 1");
        assertTrue(resultSet.next(), "ResultSet should have at least one row");
        assertEquals("John Doe", resultSet.getString("name"), "Name should remain 'John Doe'");
        assertEquals(30, resultSet.getInt("age"), "Age should remain 30");
    }

    @Test
    public void testUpdateFieldsWithNullValues() {
        assertThrows(IllegalArgumentException.class, () -> {
            dbConnection.updateFields("test_table", new String[]{"name", "age"}, null, "id = 1");
        }, "Passing null values should throw IllegalArgumentException");
    }

    @Test
    public void testUpdateFieldsWithMismatchedFieldsAndValues() {
        assertThrows(IllegalArgumentException.class, () -> {
            dbConnection.updateFields("test_table", new String[]{"name"}, new Object[]{"Jane Doe", 25}, "id = 1");
        }, "Mismatched fields and values length should throw IllegalArgumentException");
    }
}