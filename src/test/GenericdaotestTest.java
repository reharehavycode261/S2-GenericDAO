package test;

import database.core.DBConnection;
import database.core.Database;
import database.provider.PostgreSQL;
import database.core.GenericDAO;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

public class GenericDAOTest {

    private static Connection connection;
    private static GenericDAO<SomeEntity> dao;

    @BeforeAll
    public static void setup() throws Exception {
        Database database = new PostgreSQL("localhost", "5432", "dao", "", "");
        DBConnection dbConnection = database.createConnection();
        connection = dbConnection.getConnection();
        dao = new GenericDAO<>(connection, "some_entity_table");
    }

    @Test
    public void testCount() throws Exception {
        long count = dao.count();
        assertTrue(count >= 0, "The count should be zero or more");
    }

    @Test
    public void testCountWithNoRecords() throws Exception {
        // Assuming the table is empty for this test
        long count = dao.count();
        assertEquals(0, count, "The count should be zero when there are no records");
    }

    @Test
    public void testCountWithMultipleRecords() throws Exception {
        // Assuming the table has been pre-populated with 5 records for this test
        long count = dao.count();
        assertEquals(5, count, "The count should match the number of records in the table");
    }

    @Test
    public void testCountWithSQLException() {
        // Simulate a scenario where the connection is closed
        assertThrows(SQLException.class, () -> {
            connection.close();
            dao.count();
        }, "Expected a SQLException when trying to count with a closed connection");
    }

    @AfterAll
    public static void teardown() throws Exception {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }
}