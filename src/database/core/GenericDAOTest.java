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
        Map<String, Object> fieldsToUpdate = new HashMap<>();
        fieldsToUpdate.put("name", "John Doe");
        fieldsToUpdate.put("age", 30);

        String whereClause = "id = 1";

        genericDAO.updateFields(fieldsToUpdate, whereClause);

        verify(mockConnection, times(1)).prepareStatement("UPDATE test_table SET name = ?, age = ? WHERE id = 1");
        verify(mockPreparedStatement, times(1)).setObject(1, "John Doe");
        verify(mockPreparedStatement, times(1)).setObject(2, 30);
        verify(mockPreparedStatement, times(1)).executeUpdate();
    }

    @Test
    public void testUpdateFieldsWithEmptyFields() {
        Map<String, Object> fieldsToUpdate = new HashMap<>();
        String whereClause = "id = 1";

        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            genericDAO.updateFields(fieldsToUpdate, whereClause);
        });

        Assertions.assertEquals("Fields to update cannot be null or empty", exception.getMessage());
    }

    @Test
    public void testUpdateFieldsWithNullFields() {
        String whereClause = "id = 1";

        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            genericDAO.updateFields(null, whereClause);
        });

        Assertions.assertEquals("Fields to update cannot be null or empty", exception.getMessage());
    }

    @Test
    public void testUpdateFieldsWithEmptyWhereClause() {
        Map<String, Object> fieldsToUpdate = new HashMap<>();
        fieldsToUpdate.put("name", "John Doe");

        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            genericDAO.updateFields(fieldsToUpdate, "");
        });

        Assertions.assertEquals("Where clause cannot be null or empty", exception.getMessage());
    }

    @Test
    public void testUpdateFieldsWithNullWhereClause() {
        Map<String, Object> fieldsToUpdate = new HashMap<>();
        fieldsToUpdate.put("name", "John Doe");

        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            genericDAO.updateFields(fieldsToUpdate, null);
        });

        Assertions.assertEquals("Where clause cannot be null or empty", exception.getMessage());
    }

    @Test
    public void testUpdateFieldsSQLException() throws SQLException {
        Map<String, Object> fieldsToUpdate = new HashMap<>();
        fieldsToUpdate.put("name", "John Doe");
        String whereClause = "id = 1";

        when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("SQL error"));

        SQLException exception = Assertions.assertThrows(SQLException.class, () -> {
            genericDAO.updateFields(fieldsToUpdate, whereClause);
        });

        Assertions.assertEquals("SQL error", exception.getMessage());
    }
}