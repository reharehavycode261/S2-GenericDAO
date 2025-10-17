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

        Mockito.when(mockConnection.prepareStatement(Mockito.anyString())).thenReturn(mockPreparedStatement);
        Mockito.when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);

        genericDAO = new GenericDAO<>(mockConnection, "test_table");
    }

    @AfterEach
    void tearDown() throws SQLException {
        mockConnection.close();
    }

    @Test
    void testCountWithoutCondition() throws SQLException {
        // Setup mock to return a specific count
        Mockito.when(mockResultSet.next()).thenReturn(true);
        Mockito.when(mockResultSet.getLong(1)).thenReturn(10L);

        // Execute the method
        long count = genericDAO.count();

        // Assertions
        Assertions.assertEquals(10L, count, "The count should match the expected value of 10.");
        Mockito.verify(mockPreparedStatement).executeQuery();
    }

    @Test
    void testCountWithCondition() throws SQLException {
        // Setup mock to return a specific count
        Mockito.when(mockResultSet.next()).thenReturn(true);
        Mockito.when(mockResultSet.getLong(1)).thenReturn(5L);

        // Execute the method with a condition
        long count = genericDAO.count("status = 'active'");

        // Assertions
        Assertions.assertEquals(5L, count, "The count should match the expected value of 5 for the given condition.");
        Mockito.verify(mockPreparedStatement).executeQuery();
    }

    @Test
    void testCountWithEmptyTable() throws SQLException {
        // Setup mock to simulate an empty table
        Mockito.when(mockResultSet.next()).thenReturn(false);

        // Execute the method
        long count = genericDAO.count();

        // Assertions
        Assertions.assertEquals(0L, count, "The count should be 0 for an empty table.");
        Mockito.verify(mockPreparedStatement).executeQuery();
    }

    @Test
    void testCountWithSQLException() throws SQLException {
        // Setup mock to throw an SQLException
        Mockito.when(mockPreparedStatement.executeQuery()).thenThrow(new SQLException("Database error"));

        // Assertions
        Assertions.assertThrows(SQLException.class, () -> {
            genericDAO.count();
        }, "An SQLException should be thrown when there is a database error.");
    }
}