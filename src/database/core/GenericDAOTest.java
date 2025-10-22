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
import static org.mockito.Mockito.*;

public class GenericDAOTest {

    private GenericDAO<Object> genericDAO;
    private Connection mockConnection;
    private PreparedStatement mockPreparedStatement;

    @BeforeEach
    public void setUp() throws SQLException {
        mockConnection = Mockito.mock(Connection.class);
        mockPreparedStatement = Mockito.mock(PreparedStatement.class);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);

        genericDAO = new GenericDAO<Object>() {
            {
                connection = mockConnection;
                tableName = "test_table";
            }
        };
    }

    @AfterEach
    public void tearDown() throws SQLException {
        mockPreparedStatement.close();
        mockConnection.close();
    }

    @Test
    public void testUpdateFields_Success() throws SQLException {
        // Arrange
        Map<String, Object> fieldsToUpdate = new HashMap<>();
        fieldsToUpdate.put("name", "John Doe");
        fieldsToUpdate.put("age", 30);

        Map<String, Object> conditions = new HashMap<>();
        conditions.put("id", 1);

        // Act
        genericDAO.updateFields(fieldsToUpdate, conditions);

        // Assert
        verify(mockConnection, times(1)).prepareStatement("UPDATE test_table SET name = ?, age = ? WHERE id = ?");
        verify(mockPreparedStatement, times(1)).setObject(1, "John Doe");
        verify(mockPreparedStatement, times(1)).setObject(2, 30);
        verify(mockPreparedStatement, times(1)).setObject(3, 1);
        verify(mockPreparedStatement, times(1)).executeUpdate();
    }

    @Test
    public void testUpdateFields_EmptyFieldsToUpdate() {
        // Arrange
        Map<String, Object> fieldsToUpdate = new HashMap<>();
        Map<String, Object> conditions = new HashMap<>();
        conditions.put("id", 1);

        // Act & Assert
        Assertions.assertThrows(SQLException.class, () -> {
            genericDAO.updateFields(fieldsToUpdate, conditions);
        });
    }

    @Test
    public void testUpdateFields_EmptyConditions() {
        // Arrange
        Map<String, Object> fieldsToUpdate = new HashMap<>();
        fieldsToUpdate.put("name", "John Doe");

        Map<String, Object> conditions = new HashMap<>();

        // Act & Assert
        Assertions.assertThrows(SQLException.class, () -> {
            genericDAO.updateFields(fieldsToUpdate, conditions);
        });
    }

    @Test
    public void testUpdateFields_SQLException() throws SQLException {
        // Arrange
        Map<String, Object> fieldsToUpdate = new HashMap<>();
        fieldsToUpdate.put("name", "John Doe");

        Map<String, Object> conditions = new HashMap<>();
        conditions.put("id", 1);

        when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("Database error"));

        // Act & Assert
        Assertions.assertThrows(SQLException.class, () -> {
            genericDAO.updateFields(fieldsToUpdate, conditions);
        });
    }
}