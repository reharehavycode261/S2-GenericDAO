package database.core;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.mockito.Mockito;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.*;

public class GenericDAOTest {

    private Connection mockConnection;
    private PreparedStatement mockPreparedStatement;
    private GenericDAO<Object> genericDAO;

    @BeforeEach
    public void setUp() throws SQLException {
        mockConnection = Mockito.mock(Connection.class);
        mockPreparedStatement = Mockito.mock(PreparedStatement.class);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        genericDAO = new GenericDAO<>(mockConnection, "test_table");
    }

    @AfterEach
    public void tearDown() throws SQLException {
        mockConnection.close();
    }

    @Test
    public void testUpdateFields_Success() throws SQLException {
        Map<String, Object> fields = new HashMap<>();
        fields.put("name", "John Doe");
        fields.put("age", 30);

        genericDAO.updateFields(1L, fields);

        verify(mockConnection, times(1)).prepareStatement(anyString());
        verify(mockPreparedStatement, times(1)).setObject(1, "John Doe");
        verify(mockPreparedStatement, times(1)).setObject(2, 30);
        verify(mockPreparedStatement, times(1)).setLong(3, 1L);
        verify(mockPreparedStatement, times(1)).executeUpdate();
    }

    @Test
    public void testUpdateFields_EmptyFields() throws SQLException {
        Map<String, Object> fields = new HashMap<>();

        genericDAO.updateFields(1L, fields);

        verify(mockConnection, never()).prepareStatement(anyString());
        verify(mockPreparedStatement, never()).executeUpdate();
    }

    @Test
    public void testUpdateFields_NullFields() throws SQLException {
        genericDAO.updateFields(1L, null);

        verify(mockConnection, never()).prepareStatement(anyString());
        verify(mockPreparedStatement, never()).executeUpdate();
    }

    @Test
    public void testUpdateFields_SQLException() throws SQLException {
        Map<String, Object> fields = new HashMap<>();
        fields.put("name", "John Doe");

        when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("SQL Error"));

        SQLException exception = Assertions.assertThrows(SQLException.class, () -> {
            genericDAO.updateFields(1L, fields);
        });

        Assertions.assertEquals("SQL Error", exception.getMessage());
    }
}