package test;

import database.core.GenericDAO;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

public class GenericDAOTest {
    private static Connection connection;
    private GenericDAO<Object> dao;

    @BeforeAll
    public static void setupDatabaseConnection() throws SQLException {
        // Configure the connection here
        connection = DriverManager.getConnection("jdbc:testdb", "username", "password");
    }

    @BeforeEach
    public void setup() {
        // Assuming 'test_table' is a valid table in the test database
        dao = new GenericDAO<>(connection, "test_table");
    }

    @Test
    public void testCountWithValidData() throws SQLException {
        // Assuming the test_table has 42 records for this test
        long result = dao.count();
        assertEquals(42, result, "The count should return the correct number of records in the table.");
    }

    @Test
    public void testCountWithEmptyTable() throws SQLException {
        // Assuming the test_table is empty for this test
        // You might need to clear the table before this test
        long result = dao.count();
        assertEquals(0, result, "The count should return 0 when the table is empty.");
    }

    @Test
    public void testCountWithSQLException() {
        // Simulate a scenario where the connection is invalid
        assertThrows(SQLException.class, () -> {
            Connection invalidConnection = DriverManager.getConnection("jdbc:invalid", "username", "password");
            GenericDAO<Object> invalidDao = new GenericDAO<>(invalidConnection, "test_table");
            invalidDao.count();
        }, "An SQLException should be thrown when the connection is invalid.");
    }

    @AfterAll
    public static void closeDatabaseConnection() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }
}