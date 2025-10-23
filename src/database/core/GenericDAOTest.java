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
    public void testUpdateFieldsSuccess() throws SQLException {
        // Arrange
        Map<String, Object> fields = new HashMap<>();
        fields.put("name", "John Doe");
        fields.put("age", 30);

        Map<String, Object> criteria = new HashMap<>();
        criteria.put("id", 1);

        // Act
        genericDAO.updateFields(fields, criteria);

        // Assert
        verify(mockPreparedStatement, times(1)).setObject(1, "John Doe");
        verify(mockPreparedStatement, times(1)).setObject(2, 30);
        verify(mockPreparedStatement, times(1)).setObject(3, 1);
        verify(mockPreparedStatement, times(1)).executeUpdate();
    }

    @Test
    public void testUpdateFieldsWithEmptyFields() {
        // Arrange
        Map<String, Object> fields = new HashMap<>();
        Map<String, Object> criteria = new HashMap<>();
        criteria.put("id", 1);

        // Act & Assert
        Assertions.assertThrows(SQLException.class, () -> {
            genericDAO.updateFields(fields, criteria);
        });
    }

    @Test
    public void testUpdateFieldsWithEmptyCriteria() {
        // Arrange
        Map<String, Object> fields = new HashMap<>();
        fields.put("name", "John Doe");

        Map<String, Object> criteria = new HashMap<>();

        // Act & Assert
        Assertions.assertThrows(SQLException.class, () -> {
            genericDAO.updateFields(fields, criteria);
        });
    }

    @Test
    public void testUpdateFieldsSQLException() throws SQLException {
        // Arrange
        Map<String, Object> fields = new HashMap<>();
        fields.put("name", "John Doe");

        Map<String, Object> criteria = new HashMap<>();
        criteria.put("id", 1);

        doThrow(new SQLException("SQL error")).when(mockPreparedStatement).executeUpdate();

        // Act & Assert
        Assertions.assertThrows(SQLException.class, () -> {
            genericDAO.updateFields(fields, criteria);
        });
    }
}