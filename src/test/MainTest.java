package test;

import database.core.DBConnection;
import database.core.Database;
import database.core.GenericDAO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.mockito.Mockito;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class MainTest {

    private Connection mockConnection;
    private GenericDAO mockDAO;
    private Database mockDatabase;

    @BeforeEach
    public void setUp() throws SQLException {
        // Mock the Connection and GenericDAO
        mockConnection = Mockito.mock(Connection.class);
        mockDAO = Mockito.mock(GenericDAO.class);
        mockDatabase = new Database(mockConnection);
    }

    @AfterEach
    public void tearDown() throws SQLException {
        // Close the mock connection
        mockConnection.close();
    }

    @Test
    public void testMainWithValidConnection() throws SQLException {
        // Arrange
        when(mockDAO.count()).thenReturn(100L);
        when(mockDAO.count("your_column_name = 'some_value'")).thenReturn(10L);

        // Act
        long totalRecords = mockDAO.count();
        long filteredRecords = mockDAO.count("your_column_name = 'some_value'");

        // Assert
        assertEquals(100L, totalRecords, "Total records should be 100");
        assertEquals(10L, filteredRecords, "Filtered records should be 10");
    }

    @Test
    public void testMainWithSQLException() throws SQLException {
        // Arrange
        when(mockConnection.createStatement()).thenThrow(new SQLException("Database error"));

        // Act & Assert
        assertThrows(SQLException.class, () -> {
            Statement statement = mockConnection.createStatement();
            statement.executeQuery("SELECT * FROM your_table_name");
        }, "Expected SQLException to be thrown");
    }

    @Test
    public void testMainWithInvalidQuery() throws SQLException {
        // Arrange
        when(mockDAO.count("invalid_column = 'value'")).thenThrow(new SQLException("Invalid column"));

        // Act & Assert
        assertThrows(SQLException.class, () -> {
            mockDAO.count("invalid_column = 'value'");
        }, "Expected SQLException for invalid column");
    }
}