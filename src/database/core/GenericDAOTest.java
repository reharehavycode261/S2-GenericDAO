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
import static org.mockito.ArgumentMatchers.eq;
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

        genericDAO = new GenericDAO<>();
        genericDAO.connection = mockConnection;
        genericDAO.tableName = "test_table";
        genericDAO.primaryKeyField = "id";
    }

    @AfterEach
    void tearDown() throws SQLException {
        mockConnection.close();
    }

    @Test
    void testUpdateFieldsSuccess() throws SQLException {
        // Arrange
        Map<String, Object> fieldsToUpdate = new HashMap<>();
        fieldsToUpdate.put("name", "John Doe");
        fieldsToUpdate.put("age", 30);

        // Act
        genericDAO.updateFields(1, fieldsToUpdate);

        // Assert
        verify(mockPreparedStatement, times(1)).setObject(1, "John Doe");
        verify(mockPreparedStatement, times(1)).setObject(2, 30);
        verify(mockPreparedStatement, times(1)).setObject(3, 1);
        verify(mockPreparedStatement, times(1)).executeUpdate();
    }

    @Test
    void testUpdateFieldsEmptyFieldsToUpdate() {
        // Arrange
        Map<String, Object> fieldsToUpdate = new HashMap<>();

        // Act & Assert
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            genericDAO.updateFields(1, fieldsToUpdate);
        }, "fieldsToUpdate ne peut pas être null ou vide");
    }

    @Test
    void testUpdateFieldsNullFieldsToUpdate() {
        // Act & Assert
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            genericDAO.updateFields(1, null);
        }, "fieldsToUpdate ne peut pas être null ou vide");
    }

    @Test
    void testUpdateFieldsSQLException() throws SQLException {
        // Arrange
        Map<String, Object> fieldsToUpdate = new HashMap<>();
        fieldsToUpdate.put("name", "John Doe");
        when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("SQL Error"));

        // Act & Assert
        Assertions.assertThrows(SQLException.class, () -> {
            genericDAO.updateFields(1, fieldsToUpdate);
        }, "SQL Error");
    }
}