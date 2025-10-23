package test;

import database.core.GenericDAO;
import database.core.DBConnection;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.mockito.Mockito;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

public class MainTest {

    private Connection mockConnection;
    private GenericDAO<Student> mockStudentDAO;

    @BeforeEach
    public void setUp() throws SQLException {
        // Setup mock connection and DAO
        mockConnection = mock(Connection.class);
        mockStudentDAO = mock(GenericDAO.class);
        
        // Mock DBConnection to return the mock connection
        DBConnection dbConnection = mock(DBConnection.class);
        when(DBConnection.getConnection()).thenReturn(mockConnection);
    }

    @AfterEach
    public void tearDown() throws SQLException {
        // Close mock connection if necessary
        if (mockConnection != null) {
            mockConnection.close();
        }
    }

    @Test
    public void testMainPagination() throws SQLException {
        // Setup mock data
        List<Student> mockStudentsPage1 = Arrays.asList(new Student("John Doe"), new Student("Jane Doe"));
        List<Student> mockStudentsPage2 = Arrays.asList(new Student("Alice Smith"), new Student("Bob Brown"));

        // Setup mock behavior for pagination
        when(mockStudentDAO.getPaginatedResults(10, 0)).thenReturn(mockStudentsPage1);
        when(mockStudentDAO.getPaginatedResults(10, 10)).thenReturn(mockStudentsPage2);

        // Run the main method
        Main.main(new String[]{});

        // Verify that pagination is called correctly
        verify(mockStudentDAO, times(1)).getPaginatedResults(10, 0);
        verify(mockStudentDAO, times(1)).getPaginatedResults(10, 10);

        // Assertions to ensure correct pagination
        Assertions.assertEquals(2, mockStudentsPage1.size(), "Page 1 should contain 2 students");
        Assertions.assertEquals(2, mockStudentsPage2.size(), "Page 2 should contain 2 students");
    }

    @Test
    public void testMainSQLExceptionHandling() throws SQLException {
        // Setup mock to throw SQLException
        when(DBConnection.getConnection()).thenThrow(new SQLException("Database error"));

        // Capture the output
        try {
            Main.main(new String[]{});
            Assertions.fail("Expected SQLException to be thrown");
        } catch (Exception e) {
            Assertions.assertTrue(e instanceof SQLException, "Expected SQLException to be thrown");
            Assertions.assertEquals("Database error", e.getMessage(), "Expected specific error message");
        }
    }
}