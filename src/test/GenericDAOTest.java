package test;

import database.core.GenericDAO;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
    public void testCount() throws SQLException {
        long result = dao.count();
        // This depend on your test setup. Replace with expected value
        assertEquals(42, result);
    }

    @AfterAll
    public static void closeDatabaseConnection() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }
}