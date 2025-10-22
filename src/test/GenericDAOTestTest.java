package test;

import database.core.DBConnection;
import database.core.Database;
import database.core.GenericDAO;
import database.provider.PostgreSQL;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class GenericDAOTest {
    private static Database database;
    private static Connection connection;
    private GenericDAO<Emp> empDAO;

    @BeforeAll
    public static void setupDatabase() throws Exception {
        // Mocking the database connection
        database = mock(PostgreSQL.class);
        connection = mock(Connection.class);
        when(database.createConnection()).thenReturn(connection);
    }

    @BeforeEach
    public void setup() {
        empDAO = new GenericDAO<>(connection, "employees");
    }

    @Test
    public void testUpdateFieldsSuccess() throws SQLException {
        // Mocking PreparedStatement and ResultSet
        PreparedStatement preparedStatement = mock(PreparedStatement.class);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);

        Map<String, Object> fieldsToUpdate = new HashMap<>();
        fieldsToUpdate.put("name", "Updated Name");
        fieldsToUpdate.put("salary", 75000);

        empDAO.updateFields(1, fieldsToUpdate);

        // Verify that the correct SQL statement is executed
        verify(preparedStatement, times(1)).executeUpdate();
        verify(preparedStatement, times(1)).setObject(1, "Updated Name");
        verify(preparedStatement, times(1)).setObject(2, 75000);
        verify(preparedStatement, times(1)).setObject(3, 1);
    }

    @Test
    public void testUpdateFieldsNoFields() throws SQLException {
        // Mocking PreparedStatement
        PreparedStatement preparedStatement = mock(PreparedStatement.class);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);

        empDAO.updateFields(1, new HashMap<>());  // Should not throw an exception

        // Verify that no SQL statement is executed
        verify(preparedStatement, never()).executeUpdate();
    }

    @Test
    public void testUpdateFieldsInvalidId() {
        Map<String, Object> fieldsToUpdate = new HashMap<>();
        fieldsToUpdate.put("name", "Updated Name");

        // Expect an exception when an invalid ID is used
        assertThrows(SQLException.class, () -> {
            empDAO.updateFields(-1, fieldsToUpdate);
        });
    }

    @Test
    public void testSetupDatabaseConnection() throws Exception {
        // Verify that the database connection is created
        assertNotNull(connection, "Database connection should be established.");
    }

    @Test
    public void testTearDown() throws SQLException {
        // Close the connection
        tearDown();

        // Verify that the connection is closed
        verify(connection, times(1)).close();
    }

    @AfterAll
    public static void tearDown() throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }
}