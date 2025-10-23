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

        // Mock the behavior of the connection to return the prepared statement
        Mockito.when(mockConnection.prepareStatement(Mockito.anyString())).thenReturn(mockPreparedStatement);

        // Create an anonymous subclass of GenericDAO for testing
        genericDAO = new GenericDAO<Object>() {
            {
                this.connection = mockConnection;
                this.tableName = "test_table";
            }
        };
    }

    @AfterEach
    void tearDown() {
        genericDAO = null;
        mockConnection = null;
        mockPreparedStatement = null;
    }

    @Test
    void testUpdateFieldsSuccess() throws SQLException {
        Map<String, Object> fieldsToUpdate = new HashMap<>();
        fieldsToUpdate.put("name", "John Doe");
        fieldsToUpdate.put("age", 30);

        genericDAO.updateFields(1, fieldsToUpdate);

        // Verify that the prepared statement was created with the correct SQL
        Mockito.verify(mockConnection).prepareStatement("UPDATE test_table SET name = ?, age = ? WHERE id = ?");

        // Verify that the parameters were set correctly
        Mockito.verify(mockPreparedStatement).setObject(1, "John Doe");
        Mockito.verify(mockPreparedStatement).setObject(2, 30);
        Mockito.verify(mockPreparedStatement).setObject(3, 1);

        // Verify that executeUpdate was called
        Mockito.verify(mockPreparedStatement).executeUpdate();
    }

    @Test
    void testUpdateFieldsWithEmptyFields() {
        Map<String, Object> fieldsToUpdate = new HashMap<>();

        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            genericDAO.updateFields(1, fieldsToUpdate);
        });

        Assertions.assertEquals("fieldsToUpdate ne doit pas être nul ou vide", exception.getMessage());
    }

    @Test
    void testUpdateFieldsWithNullFields() {
        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            genericDAO.updateFields(1, null);
        });

        Assertions.assertEquals("fieldsToUpdate ne doit pas être nul ou vide", exception.getMessage());
    }

    @Test
    void testUpdateFieldsSQLException() throws SQLException {
        Map<String, Object> fieldsToUpdate = new HashMap<>();
        fieldsToUpdate.put("name", "John Doe");

        // Simulate an SQL exception when preparing the statement
        Mockito.when(mockConnection.prepareStatement(Mockito.anyString())).thenThrow(new SQLException("SQL error"));

        SQLException exception = Assertions.assertThrows(SQLException.class, () -> {
            genericDAO.updateFields(1, fieldsToUpdate);
        });

        Assertions.assertEquals("SQL error", exception.getMessage());
    }
}