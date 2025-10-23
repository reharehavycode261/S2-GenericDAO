package database.core;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.mockito.Mockito;

import java.sql.*;
import java.util.List;
import java.util.ArrayList;

public class GenericDAOTest {

    private Connection connection;
    private GenericDAO<TestEntity> genericDAO;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;

    private static class TestEntity {
        // TestEntity fields and methods
    }

    @BeforeEach
    public void setUp() throws SQLException {
        connection = Mockito.mock(Connection.class);
        genericDAO = new GenericDAO<>(connection, "test_table", TestEntity.class);
        preparedStatement = Mockito.mock(PreparedStatement.class);
        resultSet = Mockito.mock(ResultSet.class);
    }

    @Test
    public void testFindAllPaginated() throws SQLException {
        // Arrange
        Mockito.when(connection.prepareStatement(Mockito.anyString())).thenReturn(preparedStatement);
        Mockito.when(preparedStatement.executeQuery()).thenReturn(resultSet);
        Mockito.when(resultSet.next()).thenReturn(false); // No results

        // Act
        List<TestEntity> results = genericDAO.findAllPaginated(1, 10);

        // Assert
        Assertions.assertNotNull(results, "The result list should not be null");
        Assertions.assertTrue(results.isEmpty(), "The result list should be empty when no data is returned");
        Mockito.verify(preparedStatement).setInt(1, 10);
        Mockito.verify(preparedStatement).setInt(2, 0);
    }

    @Test
    public void testFindAllPaginatedWithResults() throws SQLException {
        // Arrange
        Mockito.when(connection.prepareStatement(Mockito.anyString())).thenReturn(preparedStatement);
        Mockito.when(preparedStatement.executeQuery()).thenReturn(resultSet);
        Mockito.when(resultSet.next()).thenReturn(true).thenReturn(false); // One result
        Mockito.when(resultSet.getObject(Mockito.anyInt(), Mockito.eq(TestEntity.class))).thenReturn(new TestEntity());

        // Act
        List<TestEntity> results = genericDAO.findAllPaginated(1, 10);

        // Assert
        Assertions.assertNotNull(results, "The result list should not be null");
        Assertions.assertEquals(1, results.size(), "The result list should contain one element");
    }

    @Test
    public void testFindAllPaginatedSQLException() throws SQLException {
        // Arrange
        Mockito.when(connection.prepareStatement(Mockito.anyString())).thenThrow(new SQLException("Database error"));

        // Act & Assert
        Assertions.assertThrows(SQLException.class, () -> {
            genericDAO.findAllPaginated(1, 10);
        }, "A SQLException should be thrown when the query fails");
    }

    @AfterEach
    public void tearDown() throws SQLException {
        preparedStatement.close();
        connection.close();
    }
}