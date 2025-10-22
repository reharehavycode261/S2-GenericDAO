import database.core.DBConnection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class DBConnectionTest {

    private DBConnection dbConnection;
    private Connection mockConnection;
    private PreparedStatement mockPreparedStatement;

    @BeforeEach
    public void setUp() throws SQLException {
        mockConnection = mock(Connection.class);
        mockPreparedStatement = mock(PreparedStatement.class);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        dbConnection = new DBConnection(null, mockConnection);
    }

    @Test
    public void testUpdateFields() throws SQLException {
        String[] fields = {"name", "email"};
        Object[] values = {"John Doe", "john.doe@example.com"};
        String condition = "id = 1";

        dbConnection.updateFields("users", fields, values, condition);

        verify(mockConnection).prepareStatement("UPDATE users SET name = ?, email = ? WHERE id = 1");
        verify(mockPreparedStatement).setObject(1, "John Doe");
        verify(mockPreparedStatement).setObject(2, "john.doe@example.com");
        verify(mockPreparedStatement).executeUpdate();
    }

    @Test
    public void testUpdateFieldsFieldsValuesMismatch() {
        String[] fields = {"name"};
        Object[] values = {"John Doe", "john.doe@example.com"};
        String condition = "id = 1";

        assertThrows(IllegalArgumentException.class, () -> {
            dbConnection.updateFields("users", fields, values, condition);
        });
    }

    @Test
    public void testUpdateFieldsWithEmptyFields() {
        String[] fields = {};
        Object[] values = {};
        String condition = "id = 1";

        assertThrows(IllegalArgumentException.class, () -> {
            dbConnection.updateFields("users", fields, values, condition);
        });
    }

    @Test
    public void testUpdateFieldsWithNullFields() {
        String[] fields = null;
        Object[] values = null;
        String condition = "id = 1";

        assertThrows(IllegalArgumentException.class, () -> {
            dbConnection.updateFields("users", fields, values, condition);
        });
    }

    @Test
    public void testUpdateFieldsSQLException() throws SQLException {
        String[] fields = {"name", "email"};
        Object[] values = {"John Doe", "john.doe@example.com"};
        String condition = "id = 1";

        when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("Database error"));

        assertThrows(SQLException.class, () -> {
            dbConnection.updateFields("users", fields, values, condition);
        });
    }
}