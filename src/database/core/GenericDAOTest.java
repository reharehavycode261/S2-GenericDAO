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

    private Connection mockConnection;
    private PreparedStatement mockPreparedStatement;
    private GenericDAO<Object> genericDAO;

    @BeforeEach
    void setUp() throws SQLException {
        mockConnection = Mockito.mock(Connection.class);
        mockPreparedStatement = Mockito.mock(PreparedStatement.class);
        Mockito.when(mockConnection.prepareStatement(Mockito.anyString())).thenReturn(mockPreparedStatement);
        genericDAO = new GenericDAO<>(mockConnection);
    }

    @AfterEach
    void tearDown() throws SQLException {
        Mockito.verifyNoMoreInteractions(mockConnection, mockPreparedStatement);
    }

    @Test
    void testUpdateFieldsSuccess() throws SQLException {
        // Arrange
        String id = "123";
        Map<String, Object> fieldsToUpdate = new HashMap<>();
        fieldsToUpdate.put("name", "John Doe");
        fieldsToUpdate.put("age", 30);

        // Act
        genericDAO.updateFields(id, fieldsToUpdate);

        // Assert
        Mockito.verify(mockConnection).prepareStatement(Mockito.anyString());
        Mockito.verify(mockPreparedStatement).setObject(1, "John Doe");
        Mockito.verify(mockPreparedStatement).setObject(2, 30);
        Mockito.verify(mockPreparedStatement).setString(3, id);
        Mockito.verify(mockPreparedStatement).executeUpdate();
    }

    @Test
    void testUpdateFieldsThrowsSQLException() throws SQLException {
        // Arrange
        String id = "123";
        Map<String, Object> fieldsToUpdate = new HashMap<>();
        fieldsToUpdate.put("name", "John Doe");

        Mockito.when(mockConnection.prepareStatement(Mockito.anyString())).thenThrow(new SQLException("Database error"));

        // Act & Assert
        Assertions.assertThrows(SQLException.class, () -> {
            genericDAO.updateFields(id, fieldsToUpdate);
        });
    }

    @Test
    void testUpdateFieldsWithEmptyFieldsToUpdate() {
        // Arrange
        String id = "123";
        Map<String, Object> fieldsToUpdate = new HashMap<>();

        // Act & Assert
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            genericDAO.updateFields(id, fieldsToUpdate);
        });
    }

    @Test
    void testUpdateFieldsWithNullFieldsToUpdate() {
        // Arrange
        String id = "123";

        // Act & Assert
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            genericDAO.updateFields(id, null);
        });
    }
}