package database.core;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;

import java.sql.*;
import java.util.List;
import java.util.ArrayList;

import static org.mockito.Mockito.*;

public class GenericDAOTest {

    private Connection mockConnection;
    private PreparedStatement mockPreparedStatement;
    private ResultSet mockResultSet;
    private GenericDAO<Object> genericDAO;
    private final String tableName = "test_table";

    @BeforeEach
    public void setUp() throws SQLException {
        mockConnection = mock(Connection.class);
        mockPreparedStatement = mock(PreparedStatement.class);
        mockResultSet = mock(ResultSet.class);
        genericDAO = new GenericDAO<>(mockConnection, tableName);

        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
    }

    @AfterEach
    public void tearDown() throws SQLException {
        mockConnection.close();
    }

    @Test
    public void testGetPaginatedResultsReturnsCorrectResults() throws SQLException {
        // Setup mock ResultSet
        when(mockResultSet.next()).thenReturn(true, true, false);
        when(mockResultSet.getObject(1)).thenReturn("entity1", "entity2");

        // Execute the method
        List<Object> results = genericDAO.getPaginatedResults(2, 0);

        // Assertions
        Assertions.assertEquals(2, results.size(), "The size of the results list should be 2");
        Assertions.assertEquals("entity1", results.get(0), "The first entity should be 'entity1'");
        Assertions.assertEquals("entity2", results.get(1), "The second entity should be 'entity2'");
    }

    @Test
    public void testGetPaginatedResultsWithNoResults() throws SQLException {
        // Setup mock ResultSet
        when(mockResultSet.next()).thenReturn(false);

        // Execute the method
        List<Object> results = genericDAO.getPaginatedResults(2, 0);

        // Assertions
        Assertions.assertTrue(results.isEmpty(), "The results list should be empty");
    }

    @Test
    public void testGetPaginatedResultsSQLException() throws SQLException {
        // Setup mock to throw SQLException
        when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("Test SQL Exception"));

        // Execute and assert exception
        Assertions.assertThrows(SQLException.class, () -> {
            genericDAO.getPaginatedResults(2, 0);
        }, "A SQLException should be thrown");
    }

    @Test
    public void testGetPaginatedResultsNegativeLimit() {
        // Execute and assert exception
        Assertions.assertThrows(SQLException.class, () -> {
            genericDAO.getPaginatedResults(-1, 0);
        }, "A SQLException should be thrown for negative limit");
    }

    @Test
    public void testGetPaginatedResultsNegativeOffset() {
        // Execute and assert exception
        Assertions.assertThrows(SQLException.class, () -> {
            genericDAO.getPaginatedResults(2, -1);
        }, "A SQLException should be thrown for negative offset");
    }
}