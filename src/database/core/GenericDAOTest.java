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

public class GenericDAOTest {

    private Connection mockConnection;
    private PreparedStatement mockPreparedStatement;
    private GenericDAO<Object> genericDAO;

    @BeforeEach
    public void setUp() throws SQLException {
        // Setup mock connection and prepared statement
        mockConnection = Mockito.mock(Connection.class);
        mockPreparedStatement = Mockito.mock(PreparedStatement.class);
        Mockito.when(mockConnection.prepareStatement(Mockito.anyString())).thenReturn(mockPreparedStatement);

        // Initialize GenericDAO with mock connection
        genericDAO = new GenericDAO<>(mockConnection, "test_table");
    }

    @AfterEach
    public void tearDown() throws SQLException {
        // Close mocks if needed
        Mockito.verify(mockPreparedStatement, Mockito.atLeastOnce()).close();
    }

    @Test
    public void testUpdateFields_Success() throws SQLException {
        // Prepare test data
        int id = 1;
        Map<String, Object> fieldsToUpdate = new HashMap<>();
        fieldsToUpdate.put("name", "John Doe");
        fieldsToUpdate.put("age", 30);

        // Execute the method
        genericDAO.updateFields(id, fieldsToUpdate);

        // Verify the prepared statement was created with the correct SQL
        Mockito.verify(mockConnection).prepareStatement("UPDATE test_table SET name = ?, age = ? WHERE id = ?");
        
        // Verify the parameters were set correctly
        Mockito.verify(mockPreparedStatement).setObject(1, "John Doe");
        Mockito.verify(mockPreparedStatement).setObject(2, 30);
        Mockito.verify(mockPreparedStatement).setInt(3, id);

        // Verify executeUpdate was called
        Mockito.verify(mockPreparedStatement).executeUpdate();
    }

    @Test
    public void testUpdateFields_EmptyFields() {
        // Prepare test data
        int id = 1;
        Map<String, Object> fieldsToUpdate = new HashMap<>();

        // Execute the method and expect an IllegalArgumentException
        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            genericDAO.updateFields(id, fieldsToUpdate);
        });

        // Assert exception message
        Assertions.assertEquals("Aucun champ fourni pour la mise à jour.", exception.getMessage());
    }

    @Test
    public void testUpdateFields_NullFields() {
        // Prepare test data
        int id = 1;

        // Execute the method and expect an IllegalArgumentException
        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            genericDAO.updateFields(id, null);
        });

        // Assert exception message
        Assertions.assertEquals("Aucun champ fourni pour la mise à jour.", exception.getMessage());
    }

    @Test
    public void testUpdateFields_SQLException() throws SQLException {
        // Prepare test data
        int id = 1;
        Map<String, Object> fieldsToUpdate = new HashMap<>();
        fieldsToUpdate.put("name", "John Doe");

        // Simulate SQLException
        Mockito.when(mockConnection.prepareStatement(Mockito.anyString())).thenThrow(new SQLException("Database error"));

        // Execute the method and expect an SQLException
        SQLException exception = Assertions.assertThrows(SQLException.class, () -> {
            genericDAO.updateFields(id, fieldsToUpdate);
        });

        // Assert exception message
        Assertions.assertEquals("Database error", exception.getMessage());
    }
}