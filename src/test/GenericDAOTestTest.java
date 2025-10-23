package test;

import database.core.GenericDAO;
import database.core.Affectation;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.function.Executable;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GenericDAOTest {

    private static Connection connection;
    private static GenericDAO<Object> dao;

    @BeforeAll
    public static void setup() throws SQLException {
        // Setup in-memory database and table
        connection = DriverManager.getConnection("jdbc:h2:mem:test");
        Statement statement = connection.createStatement();
        statement.execute("CREATE TABLE test (id INT PRIMARY KEY, name VARCHAR(255), age INT)");

        // Initialize GenericDAO
        dao = new GenericDAO<>(connection, "test", "id");
    }

    @BeforeEach
    public void init() throws SQLException {
        // Clean and populate the table before each test
        Statement statement = connection.createStatement();
        statement.execute("DELETE FROM test");
        statement.execute("INSERT INTO test (id, name, age) VALUES (1, 'John Doe', 30)");
    }

    @Test
    public void testUpdateFieldsSuccessful() throws SQLException {
        // Prepare updates
        List<Affectation> updates = new ArrayList<>();
        updates.add(new Affectation("name", "John Smith"));
        updates.add(new Affectation("age", 31));

        // Execute update
        dao.updateFields(1, updates);

        // Verify the update
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery("SELECT * FROM test WHERE id = 1");
        assertTrue(rs.next(), "Record with ID 1 should exist");
        assertEquals("John Smith", rs.getString("name"), "Name should be updated to 'John Smith'");
        assertEquals(31, rs.getInt("age"), "Age should be updated to 31");
    }

    @Test
    public void testUpdateFieldsNonExistentRecord() {
        // Prepare updates
        List<Affectation> updates = new ArrayList<>();
        updates.add(new Affectation("name", "Jane Doe"));

        // Attempt to update a non-existent record
        Executable executable = () -> dao.updateFields(2, updates);

        // Verify that an exception is thrown
        SQLException exception = assertThrows(SQLException.class, executable, "Updating non-existent record should throw SQLException");
        assertTrue(exception.getMessage().contains("No data found"), "Exception message should indicate no data found");
    }

    @Test
    public void testUpdateFieldsInvalidColumn() {
        // Prepare updates with an invalid column
        List<Affectation> updates = new ArrayList<>();
        updates.add(new Affectation("invalid_column", "Some Value"));

        // Attempt to update with an invalid column
        Executable executable = () -> dao.updateFields(1, updates);

        // Verify that an exception is thrown
        SQLException exception = assertThrows(SQLException.class, executable, "Updating with invalid column should throw SQLException");
        assertTrue(exception.getMessage().contains("Column not found"), "Exception message should indicate column not found");
    }

    @AfterAll
    public static void tearDown() throws SQLException {
        // Close the connection after all tests
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }
}