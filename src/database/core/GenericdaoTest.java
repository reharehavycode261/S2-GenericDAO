import database.core.GenericDAO;
import database.core.DBConnection;
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

public class GenericDAOTest {

    private DBConnection mockDBConnection;
    private Connection mockConnection;
    private PreparedStatement mockPreparedStatement;
    private ResultSet mockResultSet;
    private GenericDAO<Object> genericDAO;

    @BeforeEach
    public void setUp() throws SQLException {
        mockDBConnection = mock(DBConnection.class);
        mockConnection = mock(Connection.class);
        mockPreparedStatement = mock(PreparedStatement.class);
        mockResultSet = mock(ResultSet.class);

        when(mockDBConnection.getConnection()).thenReturn(mockConnection);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);

        genericDAO = new GenericDAO<>(mockDBConnection, "test_table");
    }

    @AfterEach
    public void tearDown() throws SQLException {
        mockDBConnection = null;
        mockConnection = null;
        mockPreparedStatement = null;
        mockResultSet = null;
        genericDAO = null;
    }

    @Test
    public void testCountReturnsCorrectNumberOfRecords() throws SQLException {
        // Arrange
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getLong(1)).thenReturn(5L);

        // Act
        long count = genericDAO.count();

        // Assert
        Assertions.assertEquals(5L, count, "The count method should return the correct number of records.");
    }

    @Test
    public void testCountReturnsZeroWhenNoRecords() throws SQLException {
        // Arrange
        when(mockResultSet.next()).thenReturn(false);

        // Act
        long count = genericDAO.count();

        // Assert
        Assertions.assertEquals(0L, count, "The count method should return zero when there are no records.");
    }

    @Test
    public void testCountThrowsSQLException() throws SQLException {
        // Arrange
        when(mockPreparedStatement.executeQuery()).thenThrow(new SQLException("Database error"));

        // Act & Assert
        Assertions.assertThrows(SQLException.class, () -> {
            genericDAO.count();
        }, "The count method should throw a SQLException when a database error occurs.");
    }
}