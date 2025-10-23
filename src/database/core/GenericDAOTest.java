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

class GenericDAOTest {

    private GenericDAO<Object> genericDAO;
    private Connection mockConnection;
    private PreparedStatement mockPreparedStatement;

    @BeforeEach
    void setUp() throws SQLException {
        mockConnection = Mockito.mock(Connection.class);
        mockPreparedStatement = Mockito.mock(PreparedStatement.class);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);

        genericDAO = new GenericDAO<Object>() {
            {
                this.connection = mockConnection;
                this.tableName = "test_table";
            }
        };
    }

    @AfterEach
    void tearDown() throws SQLException {
        mockPreparedStatement.close();
        mockConnection.close();
    }

    @Test
    void testUpdateFieldsSuccess() throws SQLException {
        // Arrange
        Object id = 1;
        Map<String, Object> fields = new HashMap<>();
        fields.put("name", "John Doe");
        fields.put("age", 30);

        // Act
        genericDAO.updateFields(id, fields);

        // Assert
        verify(mockConnection).prepareStatement("UPDATE test_table SET name = ?, age = ? WHERE id = ?");
        verify(mockPreparedStatement).setObject(1, "John Doe");
        verify(mockPreparedStatement).setObject(2, 30);
        verify(mockPreparedStatement).setObject(3, id);
        verify(mockPreparedStatement).executeUpdate();
    }

    @Test
    void testUpdateFieldsWithEmptyFields() {
        // Arrange
        Object id = 1;
        Map<String, Object> fields = new HashMap<>();

        // Act & Assert
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            genericDAO.updateFields(id, fields);
        }, "Fields to update cannot be null or empty.");
    }

    @Test
    void testUpdateFieldsWithNullFields() {
        // Arrange
        Object id = 1;

        // Act & Assert
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            genericDAO.updateFields(id, null);
        }, "Fields to update cannot be null or empty.");
    }

    @Test
    void testUpdateFieldsSQLException() throws SQLException {
        // Arrange
        Object id = 1;
        Map<String, Object> fields = new HashMap<>();
        fields.put("name", "John Doe");

        when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("SQL error"));

        // Act & Assert
        Assertions.assertThrows(SQLException.class, () -> {
            genericDAO.updateFields(id, fields);
        }, "SQL error");
    }
}