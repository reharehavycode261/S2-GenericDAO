package database.core;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.mockito.Mockito;

import java.sql.*;
import java.util.*;

class GenericDAOTest {

    private Connection connection;
    private GenericDAO<Object> genericDAO;
    private final String tableName = "test_table";

    @BeforeEach
    void setUp() throws SQLException {
        connection = Mockito.mock(Connection.class);
        genericDAO = new GenericDAO<Object>(connection, tableName) {
            @Override
            protected Object mapResultSetToEntity(ResultSet rs) throws SQLException {
                return new Object(); // Simple stub for testing
            }
        };
    }

    @AfterEach
    void tearDown() throws SQLException {
        connection.close();
    }

    @Test
    void testFindAllPaginatedSuccess() throws SQLException {
        // Arrange
        int page = 1;
        int pageSize = 10;
        PreparedStatement preparedStatement = Mockito.mock(PreparedStatement.class);
        ResultSet resultSet = Mockito.mock(ResultSet.class);

        Mockito.when(connection.prepareStatement(Mockito.anyString())).thenReturn(preparedStatement);
        Mockito.when(preparedStatement.executeQuery()).thenReturn(resultSet);
        Mockito.when(resultSet.next()).thenReturn(true, false); // Simulate one row

        // Act
        List<Object> results = genericDAO.findAllPaginated(page, pageSize);

        // Assert
        Assertions.assertNotNull(results, "Results should not be null");
        Assertions.assertEquals(1, results.size(), "Results size should be 1");
    }

    @Test
    void testFindAllPaginatedNoResults() throws SQLException {
        // Arrange
        int page = 1;
        int pageSize = 10;
        PreparedStatement preparedStatement = Mockito.mock(PreparedStatement.class);
        ResultSet resultSet = Mockito.mock(ResultSet.class);

        Mockito.when(connection.prepareStatement(Mockito.anyString())).thenReturn(preparedStatement);
        Mockito.when(preparedStatement.executeQuery()).thenReturn(resultSet);
        Mockito.when(resultSet.next()).thenReturn(false); // Simulate no rows

        // Act
        List<Object> results = genericDAO.findAllPaginated(page, pageSize);

        // Assert
        Assertions.assertNotNull(results, "Results should not be null");
        Assertions.assertTrue(results.isEmpty(), "Results should be empty");
    }

    @Test
    void testFindAllPaginatedSQLException() throws SQLException {
        // Arrange
        int page = 1;
        int pageSize = 10;
        Mockito.when(connection.prepareStatement(Mockito.anyString())).thenThrow(new SQLException("SQL error"));

        // Act & Assert
        Assertions.assertThrows(SQLException.class, () -> {
            genericDAO.findAllPaginated(page, pageSize);
        }, "Should throw SQLException");
    }

    @Test
    void testFindAllPaginatedNegativePageNumber() {
        // Arrange
        int page = -1;
        int pageSize = 10;

        // Act & Assert
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            genericDAO.findAllPaginated(page, pageSize);
        }, "Should throw IllegalArgumentException for negative page number");
    }

    @Test
    void testFindAllPaginatedZeroPageSize() {
        // Arrange
        int page = 1;
        int pageSize = 0;

        // Act & Assert
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            genericDAO.findAllPaginated(page, pageSize);
        }, "Should throw IllegalArgumentException for zero page size");
    }
}