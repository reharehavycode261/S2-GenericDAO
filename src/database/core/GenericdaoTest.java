import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.mockito.Mockito;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import database.core.GenericDAO;

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

        // Mocking the behavior of connection, statement, and result set
        Mockito.when(mockConnection.prepareStatement(Mockito.anyString())).thenReturn(mockPreparedStatement);
        Mockito.when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
    }

    @AfterEach
    void tearDown() throws SQLException {
        mockConnection.close();
        mockPreparedStatement.close();
        mockResultSet.close();
    }

    @Test
    void testCountReturnsCorrectNumberOfRecords() throws SQLException {
        // Arrange
        Mockito.when(mockResultSet.next()).thenReturn(true);
        Mockito.when(mockResultSet.getLong(1)).thenReturn(5L);

        // Act
        long count = genericDAO.count();

        // Assert
        Assertions.assertEquals(5L, count, "The count method should return the correct number of records.");
    }

    @Test
    void testCountReturnsZeroWhenNoRecords() throws SQLException {
        // Arrange
        Mockito.when(mockResultSet.next()).thenReturn(false);

        // Act
        long count = genericDAO.count();

        // Assert
        Assertions.assertEquals(0L, count, "The count method should return zero when there are no records.");
    }

    @Test
    void testCountThrowsSQLException() throws SQLException {
        // Arrange
        Mockito.when(mockConnection.prepareStatement(Mockito.anyString())).thenThrow(new SQLException("Database error"));

        // Act & Assert
        Assertions.assertThrows(SQLException.class, () -> {
            genericDAO.count();
        }, "The count method should throw SQLException when a database error occurs.");
    }
}