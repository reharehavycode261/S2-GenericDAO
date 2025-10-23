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

class GenericDAOTest {

    private Connection mockConnection;
    private PreparedStatement mockStatement;
    private GenericDAO<Object> genericDAO;

    @BeforeEach
    void setUp() throws SQLException {
        mockConnection = Mockito.mock(Connection.class);
        mockStatement = Mockito.mock(PreparedStatement.class);
        when(mockConnection.prepareStatement(any(String.class))).thenReturn(mockStatement);
        genericDAO = new GenericDAO<>(mockConnection, "test_table");
    }

    @AfterEach
    void tearDown() throws SQLException {
        mockConnection.close();
    }

    /**
     * Test the updateFields method with valid inputs.
     */
    @Test
    void testUpdateFields_Success() throws SQLException {
        Map<String, Object> fieldsToUpdate = new HashMap<>();
        fieldsToUpdate.put("name", "John Doe");
        fieldsToUpdate.put("age", 30);

        Object mockEntity = Mockito.mock(Object.class);
        when(mockEntity.getId()).thenReturn(1);

        when(mockStatement.executeUpdate()).thenReturn(1);

        int rowsAffected = genericDAO.updateFields(mockEntity, fieldsToUpdate);

        Assertions.assertEquals(1, rowsAffected, "The number of rows affected should be 1");
        verify(mockStatement, times(1)).executeUpdate();
    }

    /**
     * Test the updateFields method with an empty fieldsToUpdate map.
     */
    @Test
    void testUpdateFields_EmptyFieldsToUpdate() {
        Map<String, Object> fieldsToUpdate = new HashMap<>();
        Object mockEntity = Mockito.mock(Object.class);

        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            genericDAO.updateFields(mockEntity, fieldsToUpdate);
        });

        Assertions.assertEquals("fieldsToUpdate cannot be null or empty", exception.getMessage());
    }

    /**
     * Test the updateFields method with a null fieldsToUpdate map.
     */
    @Test
    void testUpdateFields_NullFieldsToUpdate() {
        Object mockEntity = Mockito.mock(Object.class);

        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            genericDAO.updateFields(mockEntity, null);
        });

        Assertions.assertEquals("fieldsToUpdate cannot be null or empty", exception.getMessage());
    }

    /**
     * Test the updateFields method when an SQLException is thrown.
     */
    @Test
    void testUpdateFields_SQLException() throws SQLException {
        Map<String, Object> fieldsToUpdate = new HashMap<>();
        fieldsToUpdate.put("name", "John Doe");

        Object mockEntity = Mockito.mock(Object.class);
        when(mockEntity.getId()).thenReturn(1);

        when(mockStatement.executeUpdate()).thenThrow(new SQLException("SQL error"));

        SQLException exception = Assertions.assertThrows(SQLException.class, () -> {
            genericDAO.updateFields(mockEntity, fieldsToUpdate);
        });

        Assertions.assertEquals("SQL error", exception.getMessage());
    }
}