package test;

import database.core.DBConnection;
import database.core.GenericDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.mockito.Mockito;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import static org.mockito.Mockito.*;

public class GenericDAOTest {
    private GenericDAO<Object> dao;
    private DBConnection dbConnection;
    private Connection mockConnection;
    private Statement mockStatement;

    @BeforeEach
    public void setUp() throws SQLException {
        // Mocking the database connection and statement
        mockConnection = Mockito.mock(Connection.class);
        mockStatement = Mockito.mock(Statement.class);
        when(mockConnection.createStatement()).thenReturn(mockStatement);

        dbConnection = new DBConnection(null, mockConnection);
        dao = new GenericDAO<>();
    }

    @AfterEach
    public void tearDown() throws SQLException {
        // Close the mock connection if needed
        if (mockConnection != null) {
            mockConnection.close();
        }
    }

    @Test
    public void testUpdateFieldsSuccess() throws SQLException {
        String[] fields = {"name", "email"};
        Object[] values = {"John Doe", "john.doe@example.com"};
        String condition = "id = 1";

        // Simulate successful execution
        when(mockStatement.executeUpdate(anyString())).thenReturn(1);

        dao.updateFields(dbConnection, fields, values, condition);

        // Verify that the statement was executed
        verify(mockStatement, times(1)).executeUpdate(anyString());
    }

    @Test
    public void testUpdateFieldsNoUpdate() throws SQLException {
        String[] fields = {"name", "email"};
        Object[] values = {"John Doe", "john.doe@example.com"};
        String condition = "id = 1";

        // Simulate no rows updated
        when(mockStatement.executeUpdate(anyString())).thenReturn(0);

        dao.updateFields(dbConnection, fields, values, condition);

        // Verify that the statement was executed
        verify(mockStatement, times(1)).executeUpdate(anyString());
    }

    @Test
    public void testUpdateFieldsSQLException() throws SQLException {
        String[] fields = {"name", "email"};
        Object[] values = {"John Doe", "john.doe@example.com"};
        String condition = "id = 1";

        // Simulate an SQL exception
        when(mockStatement.executeUpdate(anyString())).thenThrow(new SQLException("Database error"));

        SQLException exception = Assertions.assertThrows(SQLException.class, () -> {
            dao.updateFields(dbConnection, fields, values, condition);
        });

        Assertions.assertEquals("Database error", exception.getMessage());
    }

    @Test
    public void testUpdateFieldsInvalidArguments() {
        String[] fields = {"name"};
        Object[] values = {"John Doe", "john.doe@example.com"};
        String condition = "id = 1";

        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            dao.updateFields(dbConnection, fields, values, condition);
        });

        Assertions.assertEquals("Fields and values must have the same length", exception.getMessage());
    }
}