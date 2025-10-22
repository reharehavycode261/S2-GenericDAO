package test;

import database.core.DBConnection;
import database.core.GenericDAO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.mockito.Mockito;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import static org.mockito.Mockito.*;

public class MainTest {

    private Connection mockConnection;
    private DBConnection mockDBConnection;
    private GenericDAO<Object> mockDAO;

    @BeforeEach
    public void setUp() throws SQLException {
        // Mocking the Connection and DBConnection
        mockConnection = mock(Connection.class);
        mockDBConnection = mock(DBConnection.class);
        mockDAO = mock(GenericDAO.class);
        
        // Stubbing the behavior of the mock connection
        when(mockDBConnection.getConnection()).thenReturn(mockConnection);
    }

    @AfterEach
    public void tearDown() throws SQLException {
        // Closing the mock connection
        mockConnection.close();
    }

    @Test
    public void testMainSuccessfulUpdate() throws SQLException {
        // Arrange
        String[] fields = {"field1", "field2"};
        Object[] values = {"newValue1", 123};
        String condition = "id = 1";

        // Act
        mockDAO.updateFields(mockDBConnection, fields, values, condition);

        // Assert
        verify(mockDAO, times(1)).updateFields(mockDBConnection, fields, values, condition);
    }

    @Test
    public void testMainSQLException() throws SQLException {
        // Arrange
        doThrow(new SQLException("Database error")).when(mockDAO).updateFields(any(), any(), any(), any());

        // Act & Assert
        Assertions.assertThrows(SQLException.class, () -> {
            String[] fields = {"field1", "field2"};
            Object[] values = {"newValue1", 123};
            String condition = "id = 1";
            mockDAO.updateFields(mockDBConnection, fields, values, condition);
        });
    }

    @Test
    public void testMainConnectionFailure() {
        // Arrange
        Properties connectionProps = new Properties();
        connectionProps.put("user", "username");
        connectionProps.put("password", "wrongpassword");

        // Act & Assert
        Assertions.assertThrows(SQLException.class, () -> {
            DriverManager.getConnection("jdbc:mysql://localhost:3306/mydatabase", connectionProps);
        });
    }
}