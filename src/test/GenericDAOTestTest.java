package test;

import database.core.GenericDAO;
import database.core.Affectation;
import org.junit.jupiter.api.*;

import java.sql.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class GenericDAOTest {
    private static Connection connection;
    private TestDAO dao;

    @BeforeAll
    static void setUpClass() throws Exception {
        // Establish the connection for tests
        connection = DriverManager.getConnection("jdbc:h2:mem:testdb", "sa", "");
        Statement stmt = connection.createStatement();
        stmt.execute("CREATE TABLE test_table (id INT PRIMARY KEY, name VARCHAR(255), age INT)");
        stmt.execute("INSERT INTO test_table (id, name, age) VALUES (1, 'John Doe', 30)");
        stmt.execute("INSERT INTO test_table (id, name, age) VALUES (2, 'Jane Doe', 25)");
    }

    @BeforeEach
    void setUp() {
        dao = new TestDAO(connection, "test_table");
    }

    @AfterAll
    static void tearDownClass() throws SQLException {
        connection.close();
    }

    @Test
    void updateFieldsTest() throws SQLException {
        List<Affectation> updates = Arrays.asList(
            new Affectation("name", "John Smith"),
            new Affectation("age", 31)
        );

        dao.updateFields(1, updates);

        // Verify updates
        String sql = "SELECT name, age FROM test_table WHERE id = 1";
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            assertTrue(rs.next(), "Record with ID 1 should exist");
            assertEquals("John Smith", rs.getString("name"), "Name should be updated to John Smith");
            assertEquals(31, rs.getInt("age"), "Age should be updated to 31");
        }
    }

    @Test
    void updateFieldsNonExistentRecordTest() {
        List<Affectation> updates = Arrays.asList(
            new Affectation("name", "Non Existent"),
            new Affectation("age", 99)
        );

        assertThrows(SQLException.class, () -> {
            dao.updateFields(999, updates);
        }, "Updating non-existent record should throw SQLException");
    }

    @Test
    void findAllWithPaginationTest() throws SQLException {
        // Retrieve first page with one record per page
        List<TestEntity> result = dao.findAllWithPagination(1, 1);
        assertEquals(1, result.size(), "First page should contain exactly one record");
        assertEquals("John Doe", result.get(0).getName(), "First record on first page should be John Doe");

        // Retrieve second page with one record per page
        result = dao.findAllWithPagination(2, 1);
        assertEquals(1, result.size(), "Second page should contain exactly one record");
        assertEquals("Jane Doe", result.get(0).getName(), "First record on second page should be Jane Doe");
    }

    @Test
    void findAllWithPaginationInvalidPageTest() {
        assertThrows(IllegalArgumentException.class, () -> {
            dao.findAllWithPagination(0, 1);
        }, "Requesting page 0 should throw IllegalArgumentException");

        assertThrows(IllegalArgumentException.class, () -> {
            dao.findAllWithPagination(1, 0);
        }, "Requesting with page size 0 should throw IllegalArgumentException");
    }

    @Test
    void findAllWithPaginationBeyondLastPageTest() throws SQLException {
        // Retrieve a page beyond the last page
        List<TestEntity> result = dao.findAllWithPagination(3, 1);
        assertTrue(result.isEmpty(), "Page beyond last should return an empty list");
    }
}