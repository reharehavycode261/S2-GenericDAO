package test;

import database.core.DBConnection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBConnectionTest {
    private DBConnection dbConnection;
    private Connection connection;

    @BeforeEach
    public void setUp() throws SQLException {
        connection = DriverManager.getConnection("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1");
        this.dbConnection = new DBConnection(connection);
        // Setup initial test table
        try (Statement stmt = dbConnection.getConnection().createStatement()) {
            stmt.execute("CREATE TABLE test_table (id INT PRIMARY KEY, name VARCHAR(255), age INT)");
            stmt.execute("INSERT INTO test_table (id, name, age) VALUES (1, 'John Doe', 30)");
        }
    }

    @AfterEach
    public void tearDown() throws SQLException {
        try (Statement stmt = dbConnection.getConnection().createStatement()) {
            stmt.execute("DROP TABLE IF EXISTS test_table");
        }
        connection.close();
    }

    @Test
    public void testUpdateFields() throws SQLException {
        // Test updating fields with valid data
        dbConnection.updateFields("test_table", new String[]{"name", "age"}, new Object[]{"Jane Doe", 28}, "id = 1");

        var rs = dbConnection.getConnection().prepareStatement("SELECT name, age FROM test_table WHERE id = 1").executeQuery();
        if (rs.next()) {
            Assertions.assertEquals("Jane Doe", rs.getString("name"), "The name should be updated to Jane Doe");
            Assertions.assertEquals(28, rs.getInt("age"), "The age should be updated to 28");
        }
    }

    @Test
    public void testUpdateFieldsWithInvalidCondition() throws SQLException {
        // Test updating fields with a condition that matches no records
        dbConnection.updateFields("test_table", new String[]{"name", "age"}, new Object[]{"Jane Doe", 28}, "id = 2");

        var rs = dbConnection.getConnection().prepareStatement("SELECT name, age FROM test_table WHERE id = 1").executeQuery();
        if (rs.next()) {
            Assertions.assertEquals("John Doe", rs.getString("name"), "The name should remain John Doe");
            Assertions.assertEquals(30, rs.getInt("age"), "The age should remain 30");
        }
    }

    @Test
    public void testUpdateFieldsWithNullValues() {
        // Test updating fields with null values
        Assertions.assertThrows(SQLException.class, () -> {
            dbConnection.updateFields("test_table", new String[]{"name", "age"}, new Object[]{null, null}, "id = 1");
        }, "Updating with null values should throw SQLException");
    }

    @Test
    public void testUpdateFieldsWithEmptyFieldsArray() {
        // Test updating with an empty fields array
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            dbConnection.updateFields("test_table", new String[]{}, new Object[]{}, "id = 1");
        }, "Updating with an empty fields array should throw IllegalArgumentException");
    }

    @Test
    public void testUpdateFieldsWithMismatchedArrayLengths() {
        // Test updating with mismatched fields and values array lengths
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            dbConnection.updateFields("test_table", new String[]{"name"}, new Object[]{"Jane Doe", 28}, "id = 1");
        }, "Mismatched fields and values array lengths should throw IllegalArgumentException");
    }
}