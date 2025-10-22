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
        // Mocking the Connection and PreparedStatement
        mockConnection = Mockito.mock(Connection.class);
        mockPreparedStatement = Mockito.mock(PreparedStatement.class);

        // When the connection prepares a statement, return the mocked PreparedStatement
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);

        // Initialize the GenericDAO with the mocked connection
        genericDAO = new GenericDAO<>();
        // Assuming there's a way to set the connection and table name in GenericDAO
        // This might be through a constructor or setter methods
        genericDAO.setConnection(mockConnection);
        genericDAO.setTableName("test_table");
    }

    @AfterEach
    public void tearDown() {
        // Reset mocks after each test
        Mockito.reset(mockConnection, mockPreparedStatement);
    }

    @Test
    public void testUpdateFieldsSuccess() throws SQLException {
        // Prepare test data
        Object id = 1;
        Map<String, Object> fieldsToUpdate = new HashMap<>();
        fieldsToUpdate.put("name", "John Doe");
        fieldsToUpdate.put("age", 30);

        // Execute the method
        genericDAO.updateFields(id, fieldsToUpdate);

        // Verify that the PreparedStatement was created with the correct SQL
        verify(mockConnection).prepareStatement("UPDATE test_table SET name = ?, age = ? WHERE id = ?");
        
        // Verify that the parameters were set correctly
        verify(mockPreparedStatement).setObject(1, "John Doe");
        verify(mockPreparedStatement).setObject(2, 30);
        verify(mockPreparedStatement).setObject(3, id);

        // Verify that executeUpdate was called
        verify(mockPreparedStatement).executeUpdate();
    }

    @Test
    public void testUpdateFieldsWithEmptyFields() {
        // Prepare test data
        Object id = 1;
        Map<String, Object> fieldsToUpdate = new HashMap<>();

        // Execute the method and expect an exception
        Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            genericDAO.updateFields(id, fieldsToUpdate);
        });

        // Verify the exception message
        Assertions.assertEquals("No fields to update", exception.getMessage());
    }

    @Test
    public void testUpdateFieldsWithNullFields() {
        // Prepare test data
        Object id = 1;

        // Execute the method and expect an exception
        Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            genericDAO.updateFields(id, null);
        });

        // Verify the exception message
        Assertions.assertEquals("No fields to update", exception.getMessage());
    }

    @Test
    public void testUpdateFieldsSQLException() throws SQLException {
        // Prepare test data
        Object id = 1;
        Map<String, Object> fieldsToUpdate = new HashMap<>();
        fieldsToUpdate.put("name", "John Doe");

        // Simulate an SQLException when preparing the statement
        when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("Database error"));

        // Execute the method and expect an exception
        Exception exception = Assertions.assertThrows(SQLException.class, () -> {
            genericDAO.updateFields(id, fieldsToUpdate);
        });

        // Verify the exception message
        Assertions.assertEquals("Database error", exception.getMessage());
    }
}