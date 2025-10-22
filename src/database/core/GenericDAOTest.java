package database.core;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.mockito.Mockito;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.*;

public class GenericDAOTest {

    private Connection mockConnection;
    private PreparedStatement mockPreparedStatement;
    private ResultSet mockResultSet;
    private GenericDAO<Object> genericDAO;

    @BeforeEach
    public void setUp() throws SQLException {
        mockConnection = Mockito.mock(Connection.class);
        mockPreparedStatement = Mockito.mock(PreparedStatement.class);
        mockResultSet = Mockito.mock(ResultSet.class);
        genericDAO = new GenericDAO<>(mockConnection, "test_table", "id");
    }

    @AfterEach
    public void tearDown() throws SQLException {
        mockConnection.close();
    }

    @Test
    public void testFindByIdReturnsObjectWhenExists() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);

        Object result = genericDAO.findById(1);

        Assertions.assertNotNull(result, "Expected a non-null object when record exists");
        verify(mockPreparedStatement).setObject(1, 1);
    }

    @Test
    public void testFindByIdReturnsNullWhenNotExists() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false);

        Object result = genericDAO.findById(1);

        Assertions.assertNull(result, "Expected null when no record exists");
        verify(mockPreparedStatement).setObject(1, 1);
    }

    @Test
    public void testUpdateFieldsReturnsTrueWhenUpdateSuccessful() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);

        Map<String, Object> fields = new HashMap<>();
        fields.put("name", "John Doe");
        fields.put("age", 30);

        boolean result = genericDAO.updateFields(1, fields);

        Assertions.assertTrue(result, "Expected true when update is successful");
        verify(mockPreparedStatement, times(3)).setObject(anyInt(), any());
    }

    @Test
    public void testUpdateFieldsReturnsFalseWhenNoFieldsProvided() throws SQLException {
        boolean result = genericDAO.updateFields(1, new HashMap<>());

        Assertions.assertFalse(result, "Expected false when no fields are provided");
        verify(mockConnection, never()).prepareStatement(anyString());
    }

    @Test
    public void testUpdateFieldsReturnsFalseWhenUpdateFails() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(0);

        Map<String, Object> fields = new HashMap<>();
        fields.put("name", "John Doe");

        boolean result = genericDAO.updateFields(1, fields);

        Assertions.assertFalse(result, "Expected false when update fails");
        verify(mockPreparedStatement, times(2)).setObject(anyInt(), any());
    }

    @Test
    public void testFindByIdThrowsSQLException() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException());

        Assertions.assertThrows(SQLException.class, () -> {
            genericDAO.findById(1);
        }, "Expected SQLException to be thrown");
    }

    @Test
    public void testUpdateFieldsThrowsSQLException() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException());

        Map<String, Object> fields = new HashMap<>();
        fields.put("name", "John Doe");

        Assertions.assertThrows(SQLException.class, () -> {
            genericDAO.updateFields(1, fields);
        }, "Expected SQLException to be thrown");
    }
}