import database.core.GenericDAO;
import database.core.DBConnection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.mockito.Mockito;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

class GenericDAOTest {

    private GenericDAO genericDAO;
    private DBConnection mockDBConnection;
    private Connection mockConnection;
    private PreparedStatement mockPreparedStatement;
    private ResultSet mockResultSet;

    @BeforeEach
    void setUp() throws SQLException {
        genericDAO = new GenericDAO();
        mockDBConnection = Mockito.mock(DBConnection.class);
        mockConnection = Mockito.mock(Connection.class);
        mockPreparedStatement = Mockito.mock(PreparedStatement.class);
        mockResultSet = Mockito.mock(ResultSet.class);

        // Mocking the DBConnection to return a mock Connection
        Mockito.when(mockDBConnection.getConnection()).thenReturn(mockConnection);
        // Mocking the Connection to return a mock PreparedStatement
        Mockito.when(mockConnection.prepareStatement(Mockito.anyString())).thenReturn(mockPreparedStatement);
        // Mocking the PreparedStatement to return a mock ResultSet
        Mockito.when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);

        // Setting the connection in GenericDAO
        genericDAO.connection = mockDBConnection;
        genericDAO.tableName = "test_table";
    }

    @Test
    void testCountReturnsCorrectNumberOfRecords() throws SQLException {
        // Setup mock to return a specific count
        Mockito.when(mockResultSet.next()).thenReturn(true);
        Mockito.when(mockResultSet.getLong(1)).thenReturn(5L);

        long count = genericDAO.count();

        Assertions.assertEquals(5L, count, "The count method should return the correct number of records.");
    }

    @Test
    void testCountReturnsZeroWhenNoRecords() throws SQLException {
        // Setup mock to simulate no records
        Mockito.when(mockResultSet.next()).thenReturn(false);

        long count = genericDAO.count();

        Assertions.assertEquals(0L, count, "The count method should return zero when there are no records.");
    }

    @Test
    void testCountThrowsSQLException() throws SQLException {
        // Setup mock to throw SQLException
        Mockito.when(mockConnection.prepareStatement(Mockito.anyString())).thenThrow(new SQLException("SQL error"));

        Assertions.assertThrows(SQLException.class, () -> {
            genericDAO.count();
        }, "The count method should throw SQLException when a SQL error occurs.");
    }
}