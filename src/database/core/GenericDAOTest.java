package database.core;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.mockito.Mockito;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class GenericDAOTest {

    @Mock
    private Connection mockConnection;

    @Mock
    private PreparedStatement mockPreparedStatement;

    @Mock
    private ResultSet mockResultSet;

    @InjectMocks
    private GenericDAO<Object> genericDAO;

    private AutoCloseable closeable;

    @BeforeEach
    public void setUp() throws SQLException {
        closeable = MockitoAnnotations.openMocks(this);
        genericDAO = new GenericDAO<Object>() {
            {
                this.connection = mockConnection;
                this.tableName = "test_table";
            }
        };
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
    }

    @AfterEach
    public void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    public void testCountWithoutCondition() throws SQLException {
        // Setup mock behavior
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getLong(1)).thenReturn(5L);

        // Execute the method
        long count = genericDAO.count(null);

        // Verify and assert
        verify(mockPreparedStatement).executeQuery();
        assertEquals(5L, count, "Count should return the correct number of records without condition");
    }

    @Test
    public void testCountWithCondition() throws SQLException {
        // Setup mock behavior
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getLong(1)).thenReturn(3L);

        // Execute the method
        long count = genericDAO.count("status = 'active'");

        // Verify and assert
        verify(mockPreparedStatement).executeQuery();
        assertEquals(3L, count, "Count should return the correct number of records with condition");
    }

    @Test
    public void testCountWithEmptyCondition() throws SQLException {
        // Setup mock behavior
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getLong(1)).thenReturn(5L);

        // Execute the method
        long count = genericDAO.count("");

        // Verify and assert
        verify(mockPreparedStatement).executeQuery();
        assertEquals(5L, count, "Count should return the correct number of records with empty condition");
    }

    @Test
    public void testCountWithSQLException() throws SQLException {
        // Setup mock behavior
        when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("Database error"));

        // Execute and assert
        SQLException exception = assertThrows(SQLException.class, () -> {
            genericDAO.count("status = 'active'");
        });

        assertEquals("Database error", exception.getMessage(), "Should throw SQLException with correct message");
    }

    @Test
    public void testCountWithNoRecords() throws SQLException {
        // Setup mock behavior
        when(mockResultSet.next()).thenReturn(false);

        // Execute the method
        long count = genericDAO.count(null);

        // Verify and assert
        verify(mockPreparedStatement).executeQuery();
        assertEquals(0L, count, "Count should return zero when there are no records");
    }
}