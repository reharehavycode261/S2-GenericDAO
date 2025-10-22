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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class GenericDAOTest {

    private Connection mockConnection;
    private PreparedStatement mockPreparedStatement;
    private GenericDAO<Object> genericDAO;

    @BeforeEach
    void setUp() throws SQLException {
        mockConnection = Mockito.mock(Connection.class);
        mockPreparedStatement = Mockito.mock(PreparedStatement.class);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        genericDAO = new GenericDAO<>(mockConnection, "test_table");
    }

    @AfterEach
    void tearDown() throws SQLException {
        mockConnection.close();
    }

    @Test
    void testUpdateFields_Success() throws SQLException {
        String[] fields = {"field1", "field2"};
        Object[] values = {123, "testValue"};
        String condition = "id = 1";

        genericDAO.updateFields(fields, values, condition);

        verify(mockConnection, times(1)).prepareStatement(eq("UPDATE test_table SET field1 = ?, field2 = ? WHERE id = 1"));
        verify(mockPreparedStatement, times(1)).setObject(1, 123);
        verify(mockPreparedStatement, times(1)).setObject(2, "testValue");
        verify(mockPreparedStatement, times(1)).executeUpdate();
    }

    @Test
    void testUpdateFields_ThrowsIllegalArgumentException() {
        String[] fields = {"field1"};
        Object[] values = {123, "extraValue"};
        String condition = "id = 1";

        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            genericDAO.updateFields(fields, values, condition);
        });

        Assertions.assertEquals("Le nombre de champs doit correspondre au nombre de valeurs.", exception.getMessage());
    }

    @Test
    void testUpdateFields_ThrowsSQLException() throws SQLException {
        String[] fields = {"field1"};
        Object[] values = {123};
        String condition = "id = 1";

        when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("Database error"));

        SQLException exception = Assertions.assertThrows(SQLException.class, () -> {
            genericDAO.updateFields(fields, values, condition);
        });

        Assertions.assertEquals("Database error", exception.getMessage());
    }

    @Test
    void testUpdateFields_EmptyFieldsAndValues() throws SQLException {
        String[] fields = {};
        Object[] values = {};
        String condition = "id = 1";

        genericDAO.updateFields(fields, values, condition);

        verify(mockConnection, times(1)).prepareStatement(eq("UPDATE test_table SET  WHERE id = 1"));
        verify(mockPreparedStatement, times(1)).executeUpdate();
    }
}