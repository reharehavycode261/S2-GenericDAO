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

class GenericDAOTest {

    private GenericDAO<Object> genericDAO;
    private Connection mockConnection;
    private PreparedStatement mockPreparedStatement;

    @BeforeEach
    void setUp() throws SQLException {
        mockConnection = Mockito.mock(Connection.class);
        mockPreparedStatement = Mockito.mock(PreparedStatement.class);
        Mockito.when(mockConnection.prepareStatement(Mockito.anyString())).thenReturn(mockPreparedStatement);

        genericDAO = new GenericDAO<Object>() {
            {
                connection = mockConnection;
                tableName = "test_table";
            }
        };
    }

    @AfterEach
    void tearDown() {
        genericDAO = null;
    }

    @Test
    void testUpdateFieldsSuccess() throws SQLException {
        // Arrange
        Map<String, Object> fields = new HashMap<>();
        fields.put("name", "John Doe");
        fields.put("age", 30);
        Object id = 1;

        // Act
        genericDAO.updateFields(id, fields);

        // Assert
        Mockito.verify(mockPreparedStatement, Mockito.times(1)).setObject(1, "John Doe");
        Mockito.verify(mockPreparedStatement, Mockito.times(1)).setObject(2, 30);
        Mockito.verify(mockPreparedStatement, Mockito.times(1)).setObject(3, id);
        Mockito.verify(mockPreparedStatement, Mockito.times(1)).executeUpdate();
    }

    @Test
    void testUpdateFieldsWithEmptyFields() {
        // Arrange
        Map<String, Object> fields = new HashMap<>();
        Object id = 1;

        // Act & Assert
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            genericDAO.updateFields(id, fields);
        }, "Le map des champs ne peut pas être vide");
    }

    @Test
    void testUpdateFieldsWithNullFields() {
        // Arrange
        Map<String, Object> fields = null;
        Object id = 1;

        // Act & Assert
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            genericDAO.updateFields(id, fields);
        }, "Le map des champs ne peut pas être vide");
    }

    @Test
    void testUpdateFieldsSQLException() throws SQLException {
        // Arrange
        Map<String, Object> fields = new HashMap<>();
        fields.put("name", "John Doe");
        Object id = 1;

        Mockito.when(mockConnection.prepareStatement(Mockito.anyString())).thenThrow(new SQLException("Database error"));

        // Act & Assert
        Assertions.assertThrows(SQLException.class, () -> {
            genericDAO.updateFields(id, fields);
        }, "Database error");
    }
}