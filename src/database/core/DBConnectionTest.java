package database.core;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.mockito.Mockito;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

public class DBConnectionTest {

    private Connection mockConnection;
    private PreparedStatement mockPreparedStatement;
    private DBConnection dbConnection;

    @BeforeEach
    public void setUp() throws SQLException {
        mockConnection = Mockito.mock(Connection.class);
        mockPreparedStatement = Mockito.mock(PreparedStatement.class);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        dbConnection = new DBConnection(mockConnection);
    }

    @AfterEach
    public void tearDown() throws SQLException {
        mockConnection.close();
    }

    @Test
    public void testUpdateFieldsSuccess() throws SQLException {
        String[] fields = {"name", "age"};
        Object[] values = {"John", 30};
        String condition = "id = 1";

        dbConnection.updateFields("users", fields, values, condition);

        verify(mockConnection, times(1)).prepareStatement(anyString());
        verify(mockPreparedStatement, times(2)).setObject(anyInt(), any());
        verify(mockPreparedStatement, times(1)).executeUpdate();
    }

    @Test
    public void testUpdateFieldsThrowsSQLException() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("Database error"));

        String[] fields = {"name"};
        Object[] values = {"John"};
        String condition = "id = 1";

        Assertions.assertThrows(SQLException.class, () -> {
            dbConnection.updateFields("users", fields, values, condition);
        });
    }

    @Test
    public void testUpdateFieldsThrowsIllegalArgumentException() {
        String[] fields = {"name"};
        Object[] values = {"John", 30};
        String condition = "id = 1";

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            dbConnection.updateFields("users", fields, values, condition);
        });
    }

    @Test
    public void testUpdateFieldsWithEmptyFieldsAndValues() throws SQLException {
        String[] fields = {};
        Object[] values = {};
        String condition = "id = 1";

        dbConnection.updateFields("users", fields, values, condition);

        verify(mockConnection, times(1)).prepareStatement("UPDATE users SET  WHERE id = 1");
        verify(mockPreparedStatement, times(1)).executeUpdate();
    }

    @Test
    public void testUpdateFieldsWithNullValues() {
        String[] fields = {"name"};
        Object[] values = null;
        String condition = "id = 1";

        Assertions.assertThrows(NullPointerException.class, () -> {
            dbConnection.updateFields("users", fields, values, condition);
        });
    }
}