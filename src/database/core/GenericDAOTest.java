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

    private GenericDAO<Object> genericDAO;
    private Connection mockConnection;
    private PreparedStatement mockPreparedStatement;

    @BeforeEach
    public void setUp() throws SQLException {
        mockConnection = Mockito.mock(Connection.class);
        mockPreparedStatement = Mockito.mock(PreparedStatement.class);
        genericDAO = new GenericDAO<>();
        genericDAO.connection = mockConnection;
        genericDAO.tableName = "test_table";

        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
    }

    @AfterEach
    public void tearDown() throws SQLException {
        mockPreparedStatement.close();
        mockConnection.close();
    }

    @Test
    public void testUpdateFieldsSuccess() throws SQLException {
        // Arrange
        Map<String, Object> fields = new HashMap<>();
        fields.put("name", "John Doe");
        fields.put("age", 30);

        // Act
        genericDAO.updateFields(1, fields);

        // Assert
        verify(mockConnection).prepareStatement("UPDATE test_table SET name = ?, age = ? WHERE id = ?");
        verify(mockPreparedStatement).setObject(1, "John Doe");
        verify(mockPreparedStatement).setObject(2, 30);
        verify(mockPreparedStatement).setObject(3, 1);
        verify(mockPreparedStatement).executeUpdate();
    }

    @Test
    public void testUpdateFieldsEmptyFields() throws SQLException {
        // Arrange
        Map<String, Object> fields = new HashMap<>();

        // Act & Assert
        Assertions.assertThrows(SQLException.class, () -> {
            genericDAO.updateFields(1, fields);
        });
    }

    @Test
    public void testUpdateFieldsSQLException() throws SQLException {
        // Arrange
        Map<String, Object> fields = new HashMap<>();
        fields.put("name", "John Doe");

        when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("Database error"));

        // Act & Assert
        Assertions.assertThrows(SQLException.class, () -> {
            genericDAO.updateFields(1, fields);
        });
    }

    @Test
    public void testUpdateFieldsNullId() throws SQLException {
        // Arrange
        Map<String, Object> fields = new HashMap<>();
        fields.put("name", "John Doe");

        // Act & Assert
        Assertions.assertThrows(SQLException.class, () -> {
            genericDAO.updateFields(null, fields);
        });
    }
}