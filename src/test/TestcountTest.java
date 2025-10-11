import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;

import database.core.Database;
import database.core.GenericDAO;
import database.exception.SQL.DatabaseSQLException;
import database.provider.PostgreSQL;

public class TestCountTest {
    
    @Mock
    private Database mockDatabase;
    
    private Student student;
    private EmptyTest emptyTest;
    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        student = new Student();
        student.setDatabase(mockDatabase);
        student.setTableName("student");
        
        emptyTest = new EmptyTest();
        emptyTest.setDatabase(mockDatabase);
        emptyTest.setTableName("empty_test");
    }
    
    @Test
    @DisplayName("Test basic count with non-empty table")
    void testBasicCount() throws DatabaseSQLException {
        // Arrange
        when(mockDatabase.executeQuery(anyString())).thenReturn(10L);
        
        // Act
        long result = student.count();
        
        // Assert
        Assertions.assertEquals(10L, result, "Basic count should return correct number of records");
        verify(mockDatabase).executeQuery(contains("SELECT COUNT(*) FROM student"));
    }
    
    @Test
    @DisplayName("Test count with condition")
    void testCountWithCondition() throws DatabaseSQLException {
        // Arrange
        when(mockDatabase.executeQuery(anyString())).thenReturn(5L);
        
        // Act
        long result = student.count("age > 20");
        
        // Assert
        Assertions.assertEquals(5L, result, "Conditional count should return filtered number of records");
        verify(mockDatabase).executeQuery(contains("WHERE age > 20"));
    }
    
    @Test
    @DisplayName("Test count with empty table")
    void testEmptyTableCount() throws DatabaseSQLException {
        // Arrange
        when(mockDatabase.executeQuery(anyString())).thenReturn(0L);
        
        // Act
        long result = emptyTest.count();
        
        // Assert
        Assertions.assertEquals(0L, result, "Empty table should return count of 0");
    }
    
    @Test
    @DisplayName("Test count with database exception")
    void testCountWithDatabaseException() {
        // Arrange
        when(mockDatabase.executeQuery(anyString()))
            .thenThrow(new DatabaseSQLException("Database error"));
        
        // Assert
        Assertions.assertThrows(DatabaseSQLException.class, () -> {
            student.count();
        }, "Should throw DatabaseSQLException when database fails");
    }
    
    @Test
    @DisplayName("Test count with null table name")
    void testCountWithNullTableName() {
        // Arrange
        Student invalidStudent = new Student();
        invalidStudent.setDatabase(mockDatabase);
        
        // Assert
        Assertions.assertThrows(IllegalStateException.class, () -> {
            invalidStudent.count();
        }, "Should throw IllegalStateException when table name is null");
    }
    
    @Test
    @DisplayName("Test count with null database")
    void testCountWithNullDatabase() {
        // Arrange
        Student invalidStudent = new Student();
        invalidStudent.setTableName("student");
        
        // Assert
        Assertions.assertThrows(IllegalStateException.class, () -> {
            invalidStudent.count();
        }, "Should throw IllegalStateException when database is null");
    }
    
    @Test
    @DisplayName("Test count with invalid condition")
    void testCountWithInvalidCondition() throws DatabaseSQLException {
        // Arrange
        when(mockDatabase.executeQuery(anyString()))
            .thenThrow(new DatabaseSQLException("Invalid SQL syntax"));
        
        // Assert
        Assertions.assertThrows(DatabaseSQLException.class, () -> {
            student.count("invalid condition");
        }, "Should throw DatabaseSQLException when condition is invalid");
    }
    
    @AfterEach
    void tearDown() {
        student = null;
        emptyTest = null;
    }
}