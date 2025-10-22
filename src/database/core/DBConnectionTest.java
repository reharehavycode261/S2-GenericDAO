package database.core;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.mockito.Mockito;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.mockito.Mockito.*;

public class DBConnectionTest {

    private DBConnection dbConnection;
    private Connection mockConnection;
    private PreparedStatement mockPreparedStatement;

    @BeforeEach
    public void setUp() throws SQLException {
        mockConnection = Mockito.mock(Connection.class);
        mockPreparedStatement = Mockito.mock(PreparedStatement.class);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);

        dbConnection = new DBConnection();
        // Suppose there is a method to set the connection for testing purposes
        dbConnection.setConnection(mockConnection);
    }

    @AfterEach
    public void tearDown() {
        dbConnection = null;
        mockConnection = null;
        mockPreparedStatement = null;
    }

    @Test
    public void testUpdateFieldsSuccess() throws SQLException {
        String tableName = "users";
        String[] fields = {"name", "email"};
        Object[] values = {"John Doe", "john.doe@example.com"};
        String condition = "id = 1";

        dbConnection.updateFields(tableName, fields, values, condition);

        verify(mockConnection).prepareStatement("UPDATE users SET name = ?, email = ? WHERE id = 1");
        verify(mockPreparedStatement).setObject(1, "John Doe");
        verify(mockPreparedStatement).setObject(2, "john.doe@example.com");
        verify(mockPreparedStatement).executeUpdate();
    }

    @Test
    public void testUpdateFieldsThrowsSQLException() throws SQLException {
        String tableName = "users";
        String[] fields = {"name"};
        Object[] values = {"John Doe"};
        String condition = "id = 1";

        when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("Database error"));

        SQLException exception = Assertions.assertThrows(SQLException.class, () -> {
            dbConnection.updateFields(tableName, fields, values, condition);
        });

        Assertions.assertEquals("Database error", exception.getMessage());
    }

    @Test
    public void testUpdateFieldsThrowsIllegalArgumentException() {
        String tableName = "users";
        String[] fields = {"name"};
        Object[] values = {"John Doe", "Extra Value"};
        String condition = "id = 1";

        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            dbConnection.updateFields(tableName, fields, values, condition);
        });

        Assertions.assertEquals("Fields and values array must have the same length", exception.getMessage());
    }
}