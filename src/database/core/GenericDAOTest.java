import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.mockito.Mockito;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;

import static org.mockito.Mockito.*;

public class GenericDAOTest {

    private Connection mockConnection;
    private PreparedStatement mockPreparedStatement;
    private ResultSet mockResultSet;
    private ResultSetMetaData mockResultSetMetaData;
    private GenericDAO<Object> genericDAO;

    @BeforeEach
    public void setUp() throws SQLException {
        mockConnection = Mockito.mock(Connection.class);
        mockPreparedStatement = Mockito.mock(PreparedStatement.class);
        mockResultSet = Mockito.mock(ResultSet.class);
        mockResultSetMetaData = Mockito.mock(ResultSetMetaData.class);

        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.getMetaData()).thenReturn(mockResultSetMetaData);

        genericDAO = new GenericDAO<>(mockConnection, "TestTable");
    }

    @AfterEach
    public void tearDown() throws SQLException {
        mockConnection.close();
    }

    @Test
    public void testGetPageReturnsCorrectNumberOfResults() throws SQLException, IllegalAccessException, InstantiationException {
        when(mockResultSetMetaData.getColumnCount()).thenReturn(1);
        when(mockResultSet.next()).thenReturn(true, true, false);
        when(mockResultSet.getObject(1)).thenReturn(new Object());

        List<Object> results = genericDAO.getPage(2, 0);

        Assertions.assertEquals(2, results.size(), "The number of results returned should be 2");
    }

    @Test
    public void testGetPageWithOffset() throws SQLException, IllegalAccessException, InstantiationException {
        when(mockResultSetMetaData.getColumnCount()).thenReturn(1);
        when(mockResultSet.next()).thenReturn(true, false);
        when(mockResultSet.getObject(1)).thenReturn(new Object());

        List<Object> results = genericDAO.getPage(1, 1);

        Assertions.assertEquals(1, results.size(), "The number of results returned should be 1");
    }

    @Test
    public void testGetPageThrowsSQLException() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("SQL error"));

        Assertions.assertThrows(SQLException.class, () -> {
            genericDAO.getPage(1, 0);
        }, "Expected getPage to throw SQLException");
    }

    @Test
    public void testGetPageHandlesInstantiationException() throws SQLException {
        when(mockResultSetMetaData.getColumnCount()).thenReturn(1);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getObject(1)).thenReturn(new Object());

        Assertions.assertThrows(InstantiationException.class, () -> {
            genericDAO.getPage(1, 0);
        }, "Expected getPage to throw InstantiationException");
    }

    @Test
    public void testGetPageHandlesIllegalAccessException() throws SQLException {
        when(mockResultSetMetaData.getColumnCount()).thenReturn(1);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getObject(1)).thenReturn(new Object());

        Assertions.assertThrows(IllegalAccessException.class, () -> {
            genericDAO.getPage(1, 0);
        }, "Expected getPage to throw IllegalAccessException");
    }
}