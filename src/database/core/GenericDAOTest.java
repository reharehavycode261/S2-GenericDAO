package database.core;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.mockito.Mockito;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

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

        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);

        genericDAO = new GenericDAO<>(mockConnection, "test_table", Object.class);
    }

    @AfterEach
    void tearDown() throws SQLException {
        mockConnection.close();
    }

    @Test
    void testFindAllPaginatedReturnsEmptyListWhenNoResults() throws SQLException {
        when(mockResultSet.next()).thenReturn(false);

        List<Object> results = genericDAO.findAllPaginated(1, 10);

        Assertions.assertNotNull(results, "The result list should not be null");
        Assertions.assertTrue(results.isEmpty(), "The result list should be empty when no results are found");
    }

    @Test
    void testFindAllPaginatedReturnsCorrectNumberOfResults() throws SQLException {
        when(mockResultSet.next()).thenReturn(true, true, false);
        when(mockResultSetToObject(mockResultSet)).thenReturn(new Object());

        List<Object> results = genericDAO.findAllPaginated(1, 10);

        Assertions.assertNotNull(results, "The result list should not be null");
        Assertions.assertEquals(2, results.size(), "The result list should contain the correct number of results");
    }

    @Test
    void testFindAllPaginatedThrowsSQLExceptionOnPrepareStatementFailure() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("Error preparing statement"));

        Assertions.assertThrows(SQLException.class, () -> {
            genericDAO.findAllPaginated(1, 10);
        }, "An SQLException should be thrown when the statement preparation fails");
    }

    @Test
    void testFindAllPaginatedThrowsSQLExceptionOnExecuteQueryFailure() throws SQLException {
        when(mockPreparedStatement.executeQuery()).thenThrow(new SQLException("Error executing query"));

        Assertions.assertThrows(SQLException.class, () -> {
            genericDAO.findAllPaginated(1, 10);
        }, "An SQLException should be thrown when the query execution fails");
    }

    @Test
    void testFindAllPaginatedWithNegativePageNumber() throws SQLException {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            genericDAO.findAllPaginated(-1, 10);
        }, "An IllegalArgumentException should be thrown when the page number is negative");
    }

    @Test
    void testFindAllPaginatedWithZeroPageSize() throws SQLException {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            genericDAO.findAllPaginated(1, 0);
        }, "An IllegalArgumentException should be thrown when the page size is zero");
    }

    // Helper method to mock resultSetToObject
    private Object mockResultSetToObject(ResultSet rs) {
        // Mock implementation of converting ResultSet to Object
        return new Object();
    }
}