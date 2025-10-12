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
        // Mocking the connection, prepared statement, and result set
        mockConnection = Mockito.mock(Connection.class);
        mockPreparedStatement = Mockito.mock(PreparedStatement.class);
        mockResultSet = Mockito.mock(ResultSet.class);

        // Setting up the behavior of the mocked objects
        Mockito.when(mockConnection.prepareStatement(Mockito.anyString())).thenReturn(mockPreparedStatement);
        Mockito.when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);

        // Initializing the GenericDAO with the mocked connection
        genericDAO = new GenericDAO<>(mockConnection, "test_table");
    }

    @AfterEach
    void tearDown() throws SQLException {
        // Closing the mocks
        mockResultSet.close();
        mockPreparedStatement.close();
        mockConnection.close();
    }

    @Test
    void testCountReturnsCorrectNumberOfRecords() throws SQLException {
        // Arrange: Set up the mock to return a specific count
        Mockito.when(mockResultSet.next()).thenReturn(true);
        Mockito.when(mockResultSet.getLong(1)).thenReturn(5L);

        // Act: Call the count method
        long count = genericDAO.count();

        // Assert: Verify the count is as expected
        Assertions.assertEquals(5L, count, "The count method should return the correct number of records.");
    }

    @Test
    void testCountReturnsZeroWhenNoRecords() throws SQLException {
        // Arrange: Set up the mock to simulate no records
        Mockito.when(mockResultSet.next()).thenReturn(false);

        // Act: Call the count method
        long count = genericDAO.count();

        // Assert: Verify the count is zero
        Assertions.assertEquals(0L, count, "The count method should return zero when there are no records.");
    }

    @Test
    void testCountThrowsSQLException() throws SQLException {
        // Arrange: Set up the mock to throw an SQLException
        Mockito.when(mockConnection.prepareStatement(Mockito.anyString())).thenThrow(new SQLException("Database error"));

        // Act & Assert: Verify that the count method throws an SQLException
        Assertions.assertThrows(SQLException.class, () -> genericDAO.count(), "The count method should throw an SQLException when a database error occurs.");
    }
}