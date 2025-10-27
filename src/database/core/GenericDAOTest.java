import database.core.GenericDAO;
import database.core.DBConnection;
import database.exception.SQL.AttributeMissingException;
import database.exception.SQL.AttributeTypeNotExistingException;
import database.exception.object.NotIdentifiedInDatabaseException;
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

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

class GenericDAOTest {

    private GenericDAO<Object> genericDAO;
    private DBConnection mockDBConnection;
    private Connection mockConnection;
    private PreparedStatement mockPreparedStatement;
    private ResultSet mockResultSet;

    @BeforeEach
    void setUp() throws SQLException {
        genericDAO = new GenericDAO<>();
        genericDAO.id = "test_table";

        mockDBConnection = Mockito.mock(DBConnection.class);
        mockConnection = Mockito.mock(Connection.class);
        mockPreparedStatement = Mockito.mock(PreparedStatement.class);
        mockResultSet = Mockito.mock(ResultSet.class);

        when(mockDBConnection.getConnection()).thenReturn(mockConnection);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
    }

    @AfterEach
    void tearDown() throws SQLException {
        mockResultSet.close();
        mockPreparedStatement.close();
        mockConnection.close();
    }

    @Test
    void testFetchWithPaginationSuccess() throws SQLException {
        when(mockResultSet.next()).thenReturn(true, false);
        when(mockResultSet.getObject(anyInt())).thenReturn(new Object());

        List<Object> results = genericDAO.fetchWithPagination(mockDBConnection, 10, 0);

        Assertions.assertNotNull(results, "Results should not be null");
        Assertions.assertEquals(1, results.size(), "Results size should be 1");
    }

    @Test
    void testFetchWithPaginationNoResults() throws SQLException {
        when(mockResultSet.next()).thenReturn(false);

        List<Object> results = genericDAO.fetchWithPagination(mockDBConnection, 10, 0);

        Assertions.assertNotNull(results, "Results should not be null");
        Assertions.assertTrue(results.isEmpty(), "Results should be empty");
    }

    @Test
    void testFetchWithPaginationSQLException() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("Test SQL Exception"));

        Assertions.assertThrows(SQLException.class, () -> {
            genericDAO.fetchWithPagination(mockDBConnection, 10, 0);
        }, "Should throw SQLException");
    }

    @Test
    void testFetchWithPaginationInvalidLimitOffset() throws SQLException {
        when(mockResultSet.next()).thenReturn(true, false);
        when(mockResultSet.getObject(anyInt())).thenReturn(new Object());

        List<Object> results = genericDAO.fetchWithPagination(mockDBConnection, -1, -1);

        Assertions.assertNotNull(results, "Results should not be null");
        Assertions.assertEquals(1, results.size(), "Results size should be 1");
    }
}