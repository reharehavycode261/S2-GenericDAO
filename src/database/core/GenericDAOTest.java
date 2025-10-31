package database.core;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.mockito.Mockito.*;

public class GenericDAOTest {

    private Connection connection;
    private GenericDAO<Object> genericDAO;
    private final String tableName = "test_table";

    @BeforeEach
    public void setUp() throws SQLException {
        // Mocking the Connection and PreparedStatement
        connection = mock(Connection.class);
        genericDAO = new GenericDAO<>(connection, tableName);
    }

    @AfterEach
    public void tearDown() throws SQLException {
        // Closing the connection if it was opened
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

    @Test
    public void testCountReturnsCorrectNumberOfRecords() throws SQLException {
        // Mocking ResultSet to return a specific count
        PreparedStatement preparedStatement = mock(PreparedStatement.class);
        ResultSet resultSet = mock(ResultSet.class);

        when(connection.prepareStatement("SELECT COUNT(*) FROM " + tableName)).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getLong(1)).thenReturn(5L);

        // Execute the count method
        long count = genericDAO.count();

        // Assert that the count is as expected
        Assertions.assertEquals(5L, count, "The count should be 5.");
    }

    @Test
    public void testCountReturnsZeroWhenNoRecords() throws SQLException {
        // Mocking ResultSet to simulate no records
        PreparedStatement preparedStatement = mock(PreparedStatement.class);
        ResultSet resultSet = mock(ResultSet.class);

        when(connection.prepareStatement("SELECT COUNT(*) FROM " + tableName)).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);

        // Execute the count method
        long count = genericDAO.count();

        // Assert that the count is zero
        Assertions.assertEquals(0L, count, "The count should be 0 when there are no records.");
    }

    @Test
    public void testCountThrowsSQLException() throws SQLException {
        // Mocking to throw SQLException
        when(connection.prepareStatement("SELECT COUNT(*) FROM " + tableName)).thenThrow(new SQLException("Database error"));

        // Assert that the SQLException is thrown
        Assertions.assertThrows(SQLException.class, () -> {
            genericDAO.count();
        }, "An SQLException should be thrown when there is a database error.");
    }
}