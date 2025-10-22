package test;

import database.core.GenericDAO;
import org.junit.jupiter.api.*;

import java.sql.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class GenericDAOTest {

    private GenericDAO<TestEntity> dao;
    private Connection connection;

    @BeforeEach
    public void setUp() throws SQLException {
        // Set up a connection and DAO for testing
        connection = DriverManager.getConnection("jdbc:h2:mem:testdb", "sa", "");
        dao = new GenericDAO<>(connection, "test_entity");
        
        // Create a test table and insert some test data
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("CREATE TABLE test_entity (id INT PRIMARY KEY, name VARCHAR(255), value INT)");
            stmt.execute("INSERT INTO test_entity (id, name, value) VALUES (1, 'Test1', 10), (2, 'Test2', 20), (3, 'Test3', 30)");
        }
    }

    @AfterEach
    public void tearDown() throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("DROP TABLE IF EXISTS test_entity");
        }
        connection.close();
    }

    @Test
    public void testUpdateFields() throws SQLException {
        Map<String, Object> fieldsToUpdate = new HashMap<>();
        fieldsToUpdate.put("name", "UpdatedName");
        boolean updated = dao.updateFields(1, fieldsToUpdate);

        assertTrue(updated, "Update should return true");

        String name;
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT name FROM test_entity WHERE id = 1")) {
            rs.next();
            name = rs.getString("name");
        }

        assertEquals("UpdatedName", name, "Name should be updated");
    }

    @Test
    public void testUpdateFieldsNonExistentId() throws SQLException {
        Map<String, Object> fieldsToUpdate = new HashMap<>();
        fieldsToUpdate.put("name", "NonExistent");
        boolean updated = dao.updateFields(999, fieldsToUpdate);

        assertFalse(updated, "Update should return false for non-existent ID");
    }

    @Test
    public void testFindWithPagination() throws SQLException {
        List<TestEntity> results = dao.findWithPagination(2, 1);

        assertEquals(2, results.size(), "Should return two results for the first page");
        assertEquals("Test2", results.get(0).getName(), "First result should be 'Test2'");
        assertEquals("Test3", results.get(1).getName(), "Second result should be 'Test3'");
    }

    @Test
    public void testFindWithPaginationOutOfBounds() throws SQLException {
        List<TestEntity> results = dao.findWithPagination(10, 2);

        assertEquals(0, results.size(), "Should return no results for out-of-bounds page");
    }

    @Test
    public void testFindWithPaginationInvalidPageSize() {
        assertThrows(IllegalArgumentException.class, () -> dao.findWithPagination(0, 1), 
                     "Should throw IllegalArgumentException for page size of zero");
    }

    @Test
    public void testFindWithPaginationInvalidPageNumber() {
        assertThrows(IllegalArgumentException.class, () -> dao.findWithPagination(1, 0), 
                     "Should throw IllegalArgumentException for page number of zero");
    }
}