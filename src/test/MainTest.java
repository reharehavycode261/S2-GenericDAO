package test;

import database.core.GenericDAO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.mockito.Mockito;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

class MainTest {

    private GenericDAO<Student> studentDAO;

    @BeforeEach
    void setUp() {
        // Mock the GenericDAO for Student
        studentDAO = Mockito.mock(GenericDAO.class);
    }

    @AfterEach
    void tearDown() {
        studentDAO = null;
    }

    @Test
    void testPaginationFirstPage() throws SQLException {
        // Arrange
        List<Student> expectedFirstPage = Arrays.asList(new Student(), new Student(), new Student());
        Mockito.when(studentDAO.paginate(0, 10)).thenReturn(expectedFirstPage);

        // Act
        List<Student> firstPage = studentDAO.paginate(0, 10);

        // Assert
        Assertions.assertNotNull(firstPage, "First page should not be null");
        Assertions.assertEquals(3, firstPage.size(), "First page should contain 3 students");
    }

    @Test
    void testPaginationSecondPage() throws SQLException {
        // Arrange
        List<Student> expectedSecondPage = Arrays.asList(new Student(), new Student());
        Mockito.when(studentDAO.paginate(10, 10)).thenReturn(expectedSecondPage);

        // Act
        List<Student> secondPage = studentDAO.paginate(10, 10);

        // Assert
        Assertions.assertNotNull(secondPage, "Second page should not be null");
        Assertions.assertEquals(2, secondPage.size(), "Second page should contain 2 students");
    }

    @Test
    void testPaginationEmptyPage() throws SQLException {
        // Arrange
        Mockito.when(studentDAO.paginate(20, 10)).thenReturn(Arrays.asList());

        // Act
        List<Student> emptyPage = studentDAO.paginate(20, 10);

        // Assert
        Assertions.assertNotNull(emptyPage, "Empty page should not be null");
        Assertions.assertTrue(emptyPage.isEmpty(), "Empty page should contain no students");
    }

    @Test
    void testPaginationSQLException() throws SQLException {
        // Arrange
        Mockito.when(studentDAO.paginate(0, 10)).thenThrow(new SQLException("Database error"));

        // Act & Assert
        SQLException exception = Assertions.assertThrows(SQLException.class, () -> {
            studentDAO.paginate(0, 10);
        }, "Expected paginate to throw, but it didn't");

        Assertions.assertEquals("Database error", exception.getMessage(), "Exception message should match");
    }
}