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

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;

public class GenericDAOTest {

    private Connection mockConnection;
    private PreparedStatement mockPreparedStatement;
    private ResultSet mockResultSet;
    private GenericDAO<Object> genericDAO;

    @BeforeEach
    public void setUp() throws SQLException {
        mockConnection = Mockito.mock(Connection.class);
        mockPreparedStatement = Mockito.mock(PreparedStatement.class);
        mockResultSet = Mockito.mock(ResultSet.class);

        Mockito.when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        Mockito.when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);

        genericDAO = new GenericDAO<>(mockConnection, "test_table");
    }

    @AfterEach
    public void tearDown() {
        mockConnection = null;
        mockPreparedStatement = null;
        mockResultSet = null;
        genericDAO = null;
    }

    @Test
    public void testFindWithPaginationValidInput() throws SQLException {
        Mockito.when(mockResultSet.next()).thenReturn(true, false);
        Mockito.when(mockResultSet.getObject(anyInt())).thenReturn(new Object());

        List<Object> results = genericDAO.findWithPagination(1, 10);

        Assertions.assertNotNull(results, "Results should not be null");
        Assertions.assertEquals(1, results.size(), "Results size should be 1");
    }

    @Test
    public void testFindWithPaginationInvalidPageNumber() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            genericDAO.findWithPagination(0, 10);
        }, "Should throw IllegalArgumentException for page number less than 1");
    }

    @Test
    public void testFindWithPaginationInvalidPageSize() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            genericDAO.findWithPagination(1, 0);
        }, "Should throw IllegalArgumentException for page size less than 1");
    }

    @Test
    public void testFindWithPaginationSQLException() throws SQLException {
        Mockito.when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("SQL Error"));

        Assertions.assertThrows(SQLException.class, () -> {
            genericDAO.findWithPagination(1, 10);
        }, "Should throw SQLException when there is an SQL error");
    }
}