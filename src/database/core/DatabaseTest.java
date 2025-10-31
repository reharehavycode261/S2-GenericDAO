package database.core;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.mockito.Mockito;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import static org.mockito.Mockito.*;

class DatabaseTest {

    private Database database;
    private Connection mockConnection;
    private Statement mockStatement;

    @BeforeEach
    void setUp() {
        // Create a mock connection and statement
        mockConnection = Mockito.mock(Connection.class);
        mockStatement = Mockito.mock(Statement.class);

        // Create an anonymous subclass of Database to test the abstract class
        database = new Database() {
            @Override
            protected Connection getConnection() throws SQLException {
                return mockConnection;
            }
        };
    }

    @AfterEach
    void tearDown() {
        database = null;
        mockConnection = null;
        mockStatement = null;
    }

    @Test
    void testGetConnectionSuccess() throws SQLException {
        // Test if getConnection returns a non-null connection
        Connection connection = database.getConnection();
        Assertions.assertNotNull(connection, "The connection should not be null");
    }

    @Test
    void testExecuteQuerySuccess() throws SQLException {
        // Setup mock behavior
        when(mockConnection.createStatement()).thenReturn(mockStatement);
        when(mockStatement.execute(anyString())).thenReturn(true);

        // Execute a sample query
        database.executeQuery("SELECT * FROM users");

        // Verify that the statement was executed
        verify(mockStatement, times(1)).execute("SELECT * FROM users");
    }

    @Test
    void testExecuteQueryThrowsSQLException() throws SQLException {
        // Setup mock behavior to throw SQLException
        when(mockConnection.createStatement()).thenThrow(new SQLException("Connection error"));

        // Assert that executeQuery throws SQLException
        Assertions.assertThrows(SQLException.class, () -> {
            database.executeQuery("SELECT * FROM users");
        }, "executeQuery should throw SQLException when connection fails");
    }
}