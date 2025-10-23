import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.mockito.Mockito;

import java.sql.*;
import java.util.*;

class GenericDAOTest {

    private Connection connectionMock;
    private PreparedStatement preparedStatementMock;
    private ResultSet resultSetMock;
    private GenericDAO<Object> genericDAOMock;

    @BeforeEach
    void setUp() throws SQLException {
        connectionMock = Mockito.mock(Connection.class);
        preparedStatementMock = Mockito.mock(PreparedStatement.class);
        resultSetMock = Mockito.mock(ResultSet.class);

        genericDAOMock = new GenericDAO<Object>() {
            @Override
            protected Object mapRow(ResultSet rs) throws SQLException {
                return new Object(); // Mocked object mapping
            }
        };

        // Inject the mocked connection into the DAO
        genericDAOMock.connection = connectionMock;
        genericDAOMock.tableName = "mock_table";

        Mockito.when(connectionMock.prepareStatement(Mockito.anyString())).thenReturn(preparedStatementMock);
        Mockito.when(preparedStatementMock.executeQuery()).thenReturn(resultSetMock);
    }

    @AfterEach
    void tearDown() throws SQLException {
        connectionMock.close();
        preparedStatementMock.close();
        resultSetMock.close();
    }

    @Test
    void testPaginateReturnsCorrectNumberOfResults() throws SQLException {
        // Setup mock result set
        Mockito.when(resultSetMock.next()).thenReturn(true, true, false); // Two results

        List<Object> results = genericDAOMock.paginate(0, 2);

        Assertions.assertEquals(2, results.size(), "The number of results returned should be 2.");
    }

    @Test
    void testPaginateWithNoResults() throws SQLException {
        // Setup mock result set
        Mockito.when(resultSetMock.next()).thenReturn(false); // No results

        List<Object> results = genericDAOMock.paginate(0, 2);

        Assertions.assertTrue(results.isEmpty(), "The results list should be empty when there are no results.");
    }

    @Test
    void testPaginateThrowsSQLException() throws SQLException {
        // Setup mock to throw SQLException
        Mockito.when(connectionMock.prepareStatement(Mockito.anyString())).thenThrow(new SQLException("Mock SQL Exception"));

        Assertions.assertThrows(SQLException.class, () -> {
            genericDAOMock.paginate(0, 2);
        }, "paginate should throw SQLException when there is a database error.");
    }

    @Test
    void testPaginateWithNegativeOffset() throws SQLException {
        // Setup mock result set
        Mockito.when(resultSetMock.next()).thenReturn(false); // No results for negative offset

        List<Object> results = genericDAOMock.paginate(-1, 2);

        Assertions.assertTrue(results.isEmpty(), "The results list should be empty when offset is negative.");
    }

    @Test
    void testPaginateWithZeroLimit() throws SQLException {
        // Setup mock result set
        Mockito.when(resultSetMock.next()).thenReturn(false); // No results for zero limit

        List<Object> results = genericDAOMock.paginate(0, 0);

        Assertions.assertTrue(results.isEmpty(), "The results list should be empty when limit is zero.");
    }
}