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

        genericDAO = new GenericDAO<>(mockConnection, "test_table");
    }

    @AfterEach
    void tearDown() throws SQLException {
        mockConnection.close();
    }

    @Test
    void testCountWithoutCondition() throws SQLException {
        // Arrange
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getLong(1)).thenReturn(5L);

        // Act
        long count = genericDAO.count(null);

        // Assert
        Assertions.assertEquals(5L, count, "The count should be 5 when no condition is provided.");
        verify(mockPreparedStatement).executeQuery();
    }

    @Test
    void testCountWithCondition() throws SQLException {
        // Arrange
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getLong(1)).thenReturn(3L);

        // Act
        long count = genericDAO.count("age > 30");

        // Assert
        Assertions.assertEquals(3L, count, "The count should be 3 when condition 'age > 30' is provided.");
        verify(mockPreparedStatement).executeQuery();
    }

    @Test
    void testCountWithEmptyCondition() throws SQLException {
        // Arrange
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getLong(1)).thenReturn(5L);

        // Act
        long count = genericDAO.count("");

        // Assert
        Assertions.assertEquals(5L, count, "The count should be 5 when an empty condition is provided.");
        verify(mockPreparedStatement).executeQuery();
    }

    @Test
    void testCountWithSQLException() throws SQLException {
        // Arrange
        when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("Database error"));

        // Act & Assert
        Assertions.assertThrows(SQLException.class, () -> genericDAO.count(null), "SQLException should be thrown when there is a database error.");
    }

    @Test
    void testCountWithNoResults() throws SQLException {
        // Arrange
        when(mockResultSet.next()).thenReturn(false);

        // Act
        long count = genericDAO.count(null);

        // Assert
        Assertions.assertEquals(0L, count, "The count should be 0 when the result set is empty.");
        verify(mockPreparedStatement).executeQuery();
    }
}