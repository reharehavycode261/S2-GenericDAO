package database.core;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.mockito.Mockito;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.mockito.InjectMocks;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class GenericDAOTest {

    @InjectMocks
    private GenericDAO genericDAO;

    private Connection mockConnection;
    private PreparedStatement mockPreparedStatement;
    private ResultSet mockResultSet;

    @BeforeEach
    void setUp() throws SQLException {
        MockitoAnnotations.openMocks(this);
        mockConnection = mock(Connection.class);
        mockPreparedStatement = mock(PreparedStatement.class);
        mockResultSet = mock(ResultSet.class);

        genericDAO = new GenericDAO();
        genericDAO.connection = mockConnection;
        genericDAO.tableName = "test_table";

        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
    }

    @AfterEach
    void tearDown() throws Exception {
        mockConnection.close();
        mockPreparedStatement.close();
        mockResultSet.close();
    }

    /**
     * Test count() method without condition
     */
    @Test
    void testCountWithoutCondition() throws SQLException {
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getLong(1)).thenReturn(5L);

        long result = genericDAO.count();

        assertEquals(5L, result, "The count without condition should return the correct number of records.");
        verify(mockConnection).prepareStatement("SELECT COUNT(*) FROM test_table");
    }

    /**
     * Test count() method with a valid condition
     */
    @Test
    void testCountWithCondition() throws SQLException {
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getLong(1)).thenReturn(3L);

        long result = genericDAO.count("status = 'active'");

        assertEquals(3L, result, "The count with condition should return the correct number of records.");
        verify(mockConnection).prepareStatement("SELECT COUNT(*) FROM test_table WHERE status = 'active'");
    }

    /**
     * Test count() method with an empty condition
     */
    @Test
    void testCountWithEmptyCondition() throws SQLException {
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getLong(1)).thenReturn(5L);

        long result = genericDAO.count("");

        assertEquals(5L, result, "The count with an empty condition should return the total number of records.");
        verify(mockConnection).prepareStatement("SELECT COUNT(*) FROM test_table");
    }

    /**
     * Test count() method with a null condition
     */
    @Test
    void testCountWithNullCondition() throws SQLException {
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getLong(1)).thenReturn(5L);

        long result = genericDAO.count(null);

        assertEquals(5L, result, "The count with a null condition should return the total number of records.");
        verify(mockConnection).prepareStatement("SELECT COUNT(*) FROM test_table");
    }

    /**
     * Test count() method when SQL Exception is thrown
     */
    @Test
    void testCountSQLException() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("Database error"));

        SQLException exception = assertThrows(SQLException.class, () -> {
            genericDAO.count();
        });

        assertEquals("Database error", exception.getMessage(), "The SQL exception message should match.");
    }

    /**
     * Test count() method when ResultSet is empty
     */
    @Test
    void testCountWithEmptyResultSet() throws SQLException {
        when(mockResultSet.next()).thenReturn(false);

        long result = genericDAO.count();

        assertEquals(0L, result, "The count should return 0 when the ResultSet is empty.");
    }
}