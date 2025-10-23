package test;

import database.core.GenericDAO;
import org.junit.jupiter.api.*;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class GenericDAOTest {
    private GenericDAO<Student> studentDAO;
    private Connection connection;

    @BeforeEach
    public void setup() throws SQLException {
        // Initialize the database connection and DAO class
        connection = DriverManager.getConnection("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1");
        studentDAO = new GenericDAO<Student>() {
            { this.connection = GenericDAOTest.this.connection; this.tableName = "Students"; }
        };
        connection.createStatement().execute("CREATE TABLE Students (id INT PRIMARY KEY, name VARCHAR(50), mark INT)");
        connection.createStatement().execute("INSERT INTO Students (id, name, mark) VALUES (1, 'John', 85)");
    }

    @Test
    public void testUpdateFields() throws SQLException {
        // Preparing the fields to update
        Map<String, Object> fields = new HashMap<>();
        fields.put("name", "Jane");
        fields.put("mark", 90);

        // Update fields
        studentDAO.updateFields(1, fields);

        // Verify update
        ResultSet resultSet = connection.createStatement().executeQuery("SELECT * FROM Students WHERE id = 1");
        if (resultSet.next()) {
            assertEquals("Jane", resultSet.getString("name"), "The name should be updated to Jane.");
            assertEquals(90, resultSet.getInt("mark"), "The mark should be updated to 90.");
        } else {
            fail("Record with id = 1 should exist.");
        }
    }

    @Test
    public void testUpdateFieldsWithInvalidId() throws SQLException {
        // Preparing the fields to update
        Map<String, Object> fields = new HashMap<>();
        fields.put("name", "Jane");
        fields.put("mark", 90);

        // Attempt to update fields for a non-existent record
        assertThrows(SQLException.class, () -> studentDAO.updateFields(99, fields), "Updating a non-existent record should throw SQLException.");
    }

    @Test
    public void testUpdateFieldsWithEmptyFields() throws SQLException {
        // Preparing empty fields map
        Map<String, Object> fields = new HashMap<>();

        // Attempt to update with empty fields
        studentDAO.updateFields(1, fields);

        // Verify no changes
        ResultSet resultSet = connection.createStatement().executeQuery("SELECT * FROM Students WHERE id = 1");
        if (resultSet.next()) {
            assertEquals("John", resultSet.getString("name"), "The name should remain unchanged.");
            assertEquals(85, resultSet.getInt("mark"), "The mark should remain unchanged.");
        } else {
            fail("Record with id = 1 should exist.");
        }
    }

    @AfterEach
    public void tearDown() throws SQLException {
        connection.createStatement().execute("DROP TABLE Students");
        connection.close();
    }
}