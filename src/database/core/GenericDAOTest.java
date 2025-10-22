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
    private GenericDAO genericDAO;

    @BeforeEach
    void setUp() throws SQLException {
        // Mocking the Connection and PreparedStatement
        mockConnection = Mockito.mock(Connection.class);
        mockPreparedStatement = Mockito.mock(PreparedStatement.class);
        Mockito.when(mockConnection.prepareStatement(Mockito.anyString())).thenReturn(mockPreparedStatement);
        
        // Initialize the GenericDAO with the mocked connection
        genericDAO = new GenericDAO(mockConnection);
    }

    @AfterEach
    void tearDown() {
        // Reset mocks after each test
        Mockito.reset(mockConnection, mockPreparedStatement);
    }

    @Test
    void testUpdateFieldsSuccess() throws SQLException {
        // Prepare test data
        String tableName = "users";
        Map<String, Object> fieldsValues = new HashMap<>();
        fieldsValues.put("name", "John Doe");
        fieldsValues.put("email", "john.doe@example.com");
        String whereClause = "id = 1";

        // Execute the method
        genericDAO.updateFields(tableName, fieldsValues, whereClause);

        // Verify the SQL statement was prepared and executed
        Mockito.verify(mockConnection).prepareStatement("UPDATE users SET name = ?, email = ? WHERE id = 1");
        Mockito.verify(mockPreparedStatement).setObject(1, "John Doe");
        Mockito.verify(mockPreparedStatement).setObject(2, "john.doe@example.com");
        Mockito.verify(mockPreparedStatement).executeUpdate();
    }

    @Test
    void testUpdateFieldsWithEmptyFieldsValues() {
        // Prepare test data
        String tableName = "users";
        Map<String, Object> fieldsValues = new HashMap<>();
        String whereClause = "id = 1";

        // Execute the method and expect an exception
        Assertions.assertThrows(SQLException.class, () -> {
            genericDAO.updateFields(tableName, fieldsValues, whereClause);
        });
    }

    @Test
    void testUpdateFieldsSQLException() throws SQLException {
        // Prepare test data
        String tableName = "users";
        Map<String, Object> fieldsValues = new HashMap<>();
        fieldsValues.put("name", "John Doe");
        String whereClause = "id = 1";

        // Simulate SQLException on prepareStatement
        Mockito.when(mockConnection.prepareStatement(Mockito.anyString())).thenThrow(new SQLException("Database error"));

        // Execute the method and expect an exception
        Assertions.assertThrows(SQLException.class, () -> {
            genericDAO.updateFields(tableName, fieldsValues, whereClause);
        });
    }

    @Test
    void testConstructor() {
        // Assert that the connection is correctly assigned
        Assertions.assertNotNull(genericDAO.connection, "Connection should be initialized");
    }
}