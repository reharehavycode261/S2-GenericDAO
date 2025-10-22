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

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.anyInt;
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
    public void testUpdateFields_SuccessfulUpdate() throws SQLException {
        // Arrange
        Map<String, Object> fields = new HashMap<>();
        fields.put("name", "John Doe");
        fields.put("age", 30);

        when(mockPreparedStatement.executeUpdate()).thenReturn(1);

        // Act
        boolean result = genericDAO.updateFields(1, fields);

        // Assert
        Assertions.assertTrue(result, "The update should succeed and return true.");
        verify(mockPreparedStatement, times(1)).executeUpdate();
    }

    @Test
    public void testUpdateFields_EmptyFields() throws SQLException {
        // Arrange
        Map<String, Object> fields = new HashMap<>();

        // Act
        boolean result = genericDAO.updateFields(1, fields);

        // Assert
        Assertions.assertFalse(result, "The update should fail and return false due to empty fields.");
        verify(mockPreparedStatement, never()).executeUpdate();
    }

    @Test
    public void testUpdateFields_NullFields() throws SQLException {
        // Act
        boolean result = genericDAO.updateFields(1, null);

        // Assert
        Assertions.assertFalse(result, "The update should fail and return false due to null fields.");
        verify(mockPreparedStatement, never()).executeUpdate();
    }

    @Test
    public void testUpdateFields_SQLException() throws SQLException {
        // Arrange
        Map<String, Object> fields = new HashMap<>();
        fields.put("name", "John Doe");

        when(mockPreparedStatement.executeUpdate()).thenThrow(new SQLException("SQL Error"));

        // Act & Assert
        Assertions.assertThrows(SQLException.class, () -> {
            genericDAO.updateFields(1, fields);
        }, "An SQLException should be thrown when the PreparedStatement execution fails.");
    }

    @Test
    public void testUpdateFields_NoRowsAffected() throws SQLException {
        // Arrange
        Map<String, Object> fields = new HashMap<>();
        fields.put("name", "John Doe");

        when(mockPreparedStatement.executeUpdate()).thenReturn(0);

        // Act
        boolean result = genericDAO.updateFields(1, fields);

        // Assert
        Assertions.assertFalse(result, "The update should return false when no rows are affected.");
        verify(mockPreparedStatement, times(1)).executeUpdate();
    }
}