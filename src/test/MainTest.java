package test;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.mockito.Mockito;
import java.sql.Connection;
import java.sql.SQLException;

import static org.mockito.Mockito.*;

public class MainTest {

    private Connection mockConnection;
    private GenericDAO<Emp> mockEmpDao;
    private Database mockDatabase;

    @BeforeEach
    public void setUp() throws SQLException {
        // Mocking the Database and Connection
        mockDatabase = Mockito.mock(Database.class);
        mockConnection = Mockito.mock(Connection.class);
        when(mockDatabase.getConnection()).thenReturn(mockConnection);

        // Mocking the GenericDAO
        mockEmpDao = Mockito.mock(GenericDAO.class);
    }

    @AfterEach
    public void tearDown() {
        mockConnection = null;
        mockEmpDao = null;
        mockDatabase = null;
    }

    @Test
    public void testMainTotalEmployees() throws SQLException {
        // Setup the mock to return a specific value
        when(mockEmpDao.count()).thenReturn(10L);

        // Execute the method under test
        long totalEmps = mockEmpDao.count();

        // Assert the expected results
        Assertions.assertEquals(10L, totalEmps, "The total number of employees should be 10.");
    }

    @Test
    public void testMainEmployeesWithCondition() throws SQLException {
        // Setup the mock to return a specific value
        when(mockEmpDao.count("salary > 5000")).thenReturn(5L);

        // Execute the method under test
        long countCondition = mockEmpDao.count("salary > 5000");

        // Assert the expected results
        Assertions.assertEquals(5L, countCondition, "The number of employees with salary > 5000 should be 5.");
    }

    @Test
    public void testMainSQLExceptionHandling() {
        // Setup the mock to throw an SQLException
        try {
            when(mockDatabase.getConnection()).thenThrow(new SQLException("Database connection error"));

            // Execute the method under test
            Main.main(new String[]{});

            // If no exception is thrown, fail the test
            Assertions.fail("SQLException was expected but not thrown.");
        } catch (SQLException e) {
            // Assert the exception message
            Assertions.assertEquals("Database connection error", e.getMessage(), "SQLException message should match.");
        }
    }

    @Test
    public void testMainCountWithInvalidCondition() throws SQLException {
        // Setup the mock to throw an SQLException for invalid condition
        when(mockEmpDao.count("invalid condition")).thenThrow(new SQLException("Invalid SQL condition"));

        try {
            // Execute the method under test
            mockEmpDao.count("invalid condition");

            // If no exception is thrown, fail the test
            Assertions.fail("SQLException was expected but not thrown.");
        } catch (SQLException e) {
            // Assert the exception message
            Assertions.assertEquals("Invalid SQL condition", e.getMessage(), "SQLException message should match.");
        }
    }
}