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
        // Setup mock objects
        mockConnection = Mockito.mock(Connection.class);
        mockPreparedStatement = Mockito.mock(PreparedStatement.class);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);

        // Initialize the GenericDAO with the mock connection
        genericDAO = new GenericDAO<>(mockConnection, "test_table");
    }

    @AfterEach
    public void tearDown() throws SQLException {
        // Verify that all mocks were used correctly
        verifyNoMoreInteractions(mockConnection, mockPreparedStatement);
    }

    @Test
    public void testUpdateFields_Success() throws SQLException {
        // Arrange
        Map<String, Object> fields = new HashMap<>();
        fields.put("name", "John Doe");
        fields.put("age", 30);

        when(mockPreparedStatement.executeUpdate()).thenReturn(1);

        // Act
        int rowsAffected = genericDAO.updateFields(1, fields);

        // Assert
        Assertions.assertEquals(1, rowsAffected, "The number of rows affected should be 1");
        verify(mockConnection).prepareStatement("UPDATE test_table SET name = ?, age = ? WHERE id = ?");
        verify(mockPreparedStatement).setObject(1, "John Doe");
        verify(mockPreparedStatement).setObject(2, 30);
        verify(mockPreparedStatement).setObject(3, 1);
        verify(mockPreparedStatement).executeUpdate();
        verify(mockPreparedStatement).close();
    }

    @Test
    public void testUpdateFields_EmptyFields() {
        // Arrange
        Map<String, Object> fields = new HashMap<>();

        // Act & Assert
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            genericDAO.updateFields(1, fields);
        }, "An IllegalArgumentException should be thrown when fields are empty");
    }

    @Test
    public void testUpdateFields_NullFields() {
        // Act & Assert
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            genericDAO.updateFields(1, null);
        }, "An IllegalArgumentException should be thrown when fields are null");
    }

    @Test
    public void testUpdateFields_SQLException() throws SQLException {
        // Arrange
        Map<String, Object> fields = new HashMap<>();
        fields.put("name", "John Doe");

        when(mockPreparedStatement.executeUpdate()).thenThrow(new SQLException("Database error"));

        // Act & Assert
        Assertions.assertThrows(SQLException.class, () -> {
            genericDAO.updateFields(1, fields);
        }, "A SQLException should be thrown when a database error occurs");

        verify(mockConnection).prepareStatement("UPDATE test_table SET name = ? WHERE id = ?");
        verify(mockPreparedStatement).setObject(1, "John Doe");
        verify(mockPreparedStatement).setObject(2, 1);
        verify(mockPreparedStatement).executeUpdate();
        verify(mockPreparedStatement).close();
    }
}