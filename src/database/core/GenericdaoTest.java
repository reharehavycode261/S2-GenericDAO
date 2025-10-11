import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@ExtendWith(MockitoExtension.class)
public class GenericDAOTest {

    @Mock
    private Database mockDatabase;
    
    @Mock
    private Connection mockConnection;
    
    @Mock
    private Statement mockStatement;
    
    @Mock
    private ResultSet mockResultSet;
    
    private GenericDAO genericDAO;

    @BeforeEach
    void setUp() {
        genericDAO = new GenericDAO();
        genericDAO.setDatabase(mockDatabase);
    }

    @Test
    @DisplayName("Test successful count operation")
    void testCountSuccess() throws SQLException {
        // Arrange
        long expectedCount = 42L;
        when(mockDatabase.getConnection()).thenReturn(mockConnection);
        when(mockConnection.createStatement()).thenReturn(mockStatement);
        when(mockStatement.executeQuery(anyString())).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getLong(1)).thenReturn(expectedCount);

        // Act
        long actualCount = genericDAO.count();

        // Assert
        assertEquals(expectedCount, actualCount, "Count should return expected number of records");
        verify(mockResultSet).close();
        verify(mockStatement).close();
        verify(mockConnection).close();
    }

    @Test
    @DisplayName("Test count with empty result set")
    void testCountEmptyResultSet() throws SQLException {
        // Arrange
        when(mockDatabase.getConnection()).thenReturn(mockConnection);
        when(mockConnection.createStatement()).thenReturn(mockStatement);
        when(mockStatement.executeQuery(anyString())).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false);

        // Act
        long actualCount = genericDAO.count();

        // Assert
        assertEquals(0L, actualCount, "Count should return 0 for empty result set");
    }

    @Test
    @DisplayName("Test count with SQL exception")
    void testCountSQLException() throws SQLException {
        // Arrange
        when(mockDatabase.getConnection()).thenReturn(mockConnection);
        when(mockConnection.createStatement()).thenThrow(new SQLException("Database error"));

        // Act & Assert
        assertThrows(SQLException.class, () -> genericDAO.count(),
                "Should throw SQLException when database error occurs");
    }

    @Test
    @DisplayName("Test database getter and setter")
    void testDatabaseGetterSetter() {
        // Arrange
        Database newDatabase = mock(Database.class);

        // Act
        genericDAO.setDatabase(newDatabase);
        Database retrievedDatabase = genericDAO.getDatabase();

        // Assert
        assertSame(newDatabase, retrievedDatabase, "Database getter should return the same instance that was set");
    }

    @Test
    @DisplayName("Test table name initialization")
    void testTableNameInitialization() {
        // Act
        String tableName = genericDAO.getTableName();

        // Assert
        assertNotNull(tableName, "Table name should not be null");
        assertFalse(tableName.isEmpty(), "Table name should not be empty");
    }

    @Test
    @DisplayName("Test resource cleanup on exception")
    void testResourceCleanupOnException() throws SQLException {
        // Arrange
        when(mockDatabase.getConnection()).thenReturn(mockConnection);
        when(mockConnection.createStatement()).thenReturn(mockStatement);
        when(mockStatement.executeQuery(anyString())).thenThrow(new SQLException("Query failed"));

        // Act & Assert
        assertThrows(SQLException.class, () -> genericDAO.count());
        verify(mockStatement).close();
        verify(mockConnection).close();
    }
}