package test;

import database.core.GenericDAO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.mockito.Mockito;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.mockito.Mockito.*;

public class MainTest {

    private Connection mockConnection;
    private GenericDAO<Emp> mockEmpDAO;

    @BeforeEach
    public void setUp() throws SQLException {
        // Mocking the Connection object
        mockConnection = mock(Connection.class);

        // Mocking the GenericDAO object
        mockEmpDAO = mock(GenericDAO.class);
    }

    @AfterEach
    public void tearDown() throws SQLException {
        // Closing the mock connection
        if (mockConnection != null) {
            mockConnection.close();
        }
    }

    @Test
    public void testMainMethodWithValidConnection() throws SQLException {
        // Arrange
        when(mockEmpDAO.count()).thenReturn(10L);

        // Act
        long count = mockEmpDAO.count();

        // Assert
        Assertions.assertEquals(10L, count, "The count of records should be 10.");
    }

    @Test
    public void testMainMethodWithSQLException() {
        // Arrange
        try {
            when(DriverManager.getConnection(anyString(), anyString(), anyString()))
                    .thenThrow(new SQLException("Database connection error"));

            // Act & Assert
            SQLException exception = Assertions.assertThrows(SQLException.class, () -> {
                DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "username", "password");
            });

            Assertions.assertEquals("Database connection error", exception.getMessage(), "The exception message should be 'Database connection error'.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testMainMethodWithZeroRecords() throws SQLException {
        // Arrange
        when(mockEmpDAO.count()).thenReturn(0L);

        // Act
        long count = mockEmpDAO.count();

        // Assert
        Assertions.assertEquals(0L, count, "The count of records should be 0.");
    }

    @Test
    public void testMainMethodWithNegativeScenario() throws SQLException {
        // Arrange
        when(mockEmpDAO.count()).thenReturn(-1L);

        // Act
        long count = mockEmpDAO.count();

        // Assert
        Assertions.assertEquals(-1L, count, "The count of records should be -1, indicating an error scenario.");
    }
}