package database.core;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.mockito.Mockito;
import org.mockito.ArgumentMatchers;

import java.sql.*;
import java.util.List;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GenericDAOTest {

    private Connection mockConnection;
    private PreparedStatement mockPreparedStatement;
    private ResultSet mockResultSet;
    private GenericDAO<Object> genericDAO;

    @BeforeEach
    void setUp() throws SQLException {
        mockConnection = Mockito.mock(Connection.class);
        mockPreparedStatement = Mockito.mock(PreparedStatement.class);
        mockResultSet = Mockito.mock(ResultSet.class);
        genericDAO = new GenericDAO<>(mockConnection, "test_table");

        when(mockConnection.prepareStatement(ArgumentMatchers.anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
    }

    @AfterEach
    void tearDown() throws SQLException {
        mockResultSet.close();
        mockPreparedStatement.close();
        mockConnection.close();
    }

    @Test
    void testFindAllReturnsEmptyListWhenNoData() throws SQLException {
        // Arrange
        when(mockResultSet.next()).thenReturn(false);

        // Act
        List<Object> result = genericDAO.findAll();

        // Assert
        assertNotNull(result, "The result should not be null");
        assertTrue(result.isEmpty(), "The result list should be empty when no data is present");
    }

    @Test
    void testFindAllReturnsDataWhenPresent() throws SQLException {
        // Arrange
        when(mockResultSet.next()).thenReturn(true).thenReturn(false);
        when(mockResultSet.getObject(anyInt())).thenReturn(new Object());

        // Act
        List<Object> result = genericDAO.findAll();

        // Assert
        assertNotNull(result, "The result should not be null");
        assertEquals(1, result.size(), "The result list should contain one item");
    }

    @Test
    void testFindPageReturnsEmptyListWhenNoData() throws SQLException {
        // Arrange
        when(mockResultSet.next()).thenReturn(false);

        // Act
        List<Object> result = genericDAO.findPage(0, 10);

        // Assert
        assertNotNull(result, "The result should not be null");
        assertTrue(result.isEmpty(), "The result list should be empty when no data is present");
    }

    @Test
    void testFindPageReturnsDataWhenPresent() throws SQLException {
        // Arrange
        when(mockResultSet.next()).thenReturn(true).thenReturn(false);
        when(mockResultSet.getObject(anyInt())).thenReturn(new Object());

        // Act
        List<Object> result = genericDAO.findPage(0, 10);

        // Assert
        assertNotNull(result, "The result should not be null");
        assertEquals(1, result.size(), "The result list should contain one item");
    }

    @Test
    void testFindPageWithNegativeOffsetThrowsSQLException() {
        // Act & Assert
        assertThrows(SQLException.class, () -> genericDAO.findPage(-1, 10), 
            "An SQLException should be thrown when offset is negative");
    }

    @Test
    void testFindPageWithNegativeLimitThrowsSQLException() {
        // Act & Assert
        assertThrows(SQLException.class, () -> genericDAO.findPage(0, -10), 
            "An SQLException should be thrown when limit is negative");
    }

    @Test
    void testFindAllThrowsSQLExceptionWhenQueryFails() throws SQLException {
        // Arrange
        when(mockPreparedStatement.executeQuery()).thenThrow(new SQLException("Query failed"));

        // Act & Assert
        assertThrows(SQLException.class, () -> genericDAO.findAll(), 
            "An SQLException should be thrown when the query execution fails");
    }

    @Test
    void testFindPageThrowsSQLExceptionWhenQueryFails() throws SQLException {
        // Arrange
        when(mockPreparedStatement.executeQuery()).thenThrow(new SQLException("Query failed"));

        // Act & Assert
        assertThrows(SQLException.class, () -> genericDAO.findPage(0, 10), 
            "An SQLException should be thrown when the query execution fails");
    }
}