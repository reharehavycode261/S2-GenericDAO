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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class GenericDAOTest {

    private Connection mockConnection;
    private PreparedStatement mockPreparedStatement;
    private GenericDAO<Object> genericDAO;

    @BeforeEach
    public void setUp() throws SQLException {
        mockConnection = Mockito.mock(Connection.class);
        mockPreparedStatement = Mockito.mock(PreparedStatement.class);
        when(mockConnection.prepareStatement(any(String.class))).thenReturn(mockPreparedStatement);

        genericDAO = new GenericDAO<>(mockConnection, "test_table");
    }

    @AfterEach
    public void tearDown() {
        mockConnection = null;
        mockPreparedStatement = null;
        genericDAO = null;
    }

    @Test
    public void testUpdateFieldsSuccess() throws SQLException {
        // Arrange
        Map<String, Object> fields = new HashMap<>();
        fields.put("name", "John Doe");
        fields.put("age", 30);

        when(mockPreparedStatement.executeUpdate()).thenReturn(1);

        // Act
        boolean result = genericDAO.updateFields(1, fields);

        // Assert
        Assertions.assertTrue(result, "The record should be updated successfully");
        verify(mockPreparedStatement, times(1)).setObject(eq(1), eq("John Doe"));
        verify(mockPreparedStatement, times(1)).setObject(eq(2), eq(30));
        verify(mockPreparedStatement, times(1)).setObject(eq(3), eq(1));
        verify(mockPreparedStatement, times(1)).executeUpdate();
    }

    @Test
    public void testUpdateFieldsNoUpdate() throws SQLException {
        // Arrange
        Map<String, Object> fields = new HashMap<>();
        fields.put("name", "John Doe");

        when(mockPreparedStatement.executeUpdate()).thenReturn(0);

        // Act
        boolean result = genericDAO.updateFields(1, fields);

        // Assert
        Assertions.assertFalse(result, "The record should not be updated");
    }

    @Test
    public void testUpdateFieldsEmptyFields() {
        // Arrange
        Map<String, Object> fields = new HashMap<>();

        // Act & Assert
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            genericDAO.updateFields(1, fields);
        }, "An IllegalArgumentException should be thrown for empty fields map");
    }

    @Test
    public void testUpdateFieldsNullFields() {
        // Act & Assert
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            genericDAO.updateFields(1, null);
        }, "An IllegalArgumentException should be thrown for null fields map");
    }

    @Test
    public void testUpdateFieldsSQLException() throws SQLException {
        // Arrange
        Map<String, Object> fields = new HashMap<>();
        fields.put("name", "John Doe");

        when(mockPreparedStatement.executeUpdate()).thenThrow(new SQLException("SQL Error"));

        // Act & Assert
        Assertions.assertThrows(SQLException.class, () -> {
            genericDAO.updateFields(1, fields);
        }, "A SQLException should be thrown for SQL errors");
    }
}