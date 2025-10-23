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

    // Test entity class for mocking purposes
    public static class TestEntity {
        private int id;
        private String name;

        public TestEntity() {}

        public int getId() { return id; }
        public void setId(int id) { this.id = id; }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
    }

    @BeforeEach
    public void setUp() throws SQLException {
        connection = Mockito.mock(Connection.class);
        preparedStatement = Mockito.mock(PreparedStatement.class);
        resultSet = Mockito.mock(ResultSet.class);
        genericDAO = new GenericDAO<>(connection, "test_table", TestEntity.class);
    }

    @AfterEach
    public void tearDown() throws SQLException {
        Mockito.verifyNoMoreInteractions(connection, preparedStatement, resultSet);
    }

    @Test
    public void testFindAllWithPagination_Success() throws Exception {
        // Arrange
        Mockito.when(connection.prepareStatement(Mockito.anyString())).thenReturn(preparedStatement);
        Mockito.when(preparedStatement.executeQuery()).thenReturn(resultSet);
        Mockito.when(resultSet.next()).thenReturn(true, true, false);
        
        // Mocking the result set for two entities
        Mockito.when(resultSet.getInt("id")).thenReturn(1, 2);
        Mockito.when(resultSet.getString("name")).thenReturn("Entity1", "Entity2");

        // Act
        List<TestEntity> results = genericDAO.findAllWithPagination(1, 2);

        // Assert
        Assertions.assertNotNull(results, "Results should not be null");
        Assertions.assertEquals(2, results.size(), "Results size should be 2");
        Assertions.assertEquals(1, results.get(0).getId(), "First entity ID should be 1");
        Assertions.assertEquals("Entity1", results.get(0).getName(), "First entity name should be 'Entity1'");
        Assertions.assertEquals(2, results.get(1).getId(), "Second entity ID should be 2");
        Assertions.assertEquals("Entity2", results.get(1).getName(), "Second entity name should be 'Entity2'");
    }

    @Test
    public void testFindAllWithPagination_EmptyResult() throws Exception {
        // Arrange
        Mockito.when(connection.prepareStatement(Mockito.anyString())).thenReturn(preparedStatement);
        Mockito.when(preparedStatement.executeQuery()).thenReturn(resultSet);
        Mockito.when(resultSet.next()).thenReturn(false);

        // Act
        List<TestEntity> results = genericDAO.findAllWithPagination(1, 2);

        // Assert
        Assertions.assertNotNull(results, "Results should not be null");
        Assertions.assertTrue(results.isEmpty(), "Results should be empty");
    }

    @Test
    public void testFindAllWithPagination_SQLException() throws Exception {
        // Arrange
        Mockito.when(connection.prepareStatement(Mockito.anyString())).thenThrow(new SQLException("SQL error"));

        // Act & Assert
        SQLException exception = Assertions.assertThrows(SQLException.class, () -> {
            genericDAO.findAllWithPagination(1, 2);
        }, "Expected findAllWithPagination to throw, but it didn't");

        Assertions.assertEquals("SQL error", exception.getMessage(), "Exception message should match");
    }

    @Test
    public void testFindAllWithPagination_InvalidPageNumber() throws Exception {
        // Arrange
        Mockito.when(connection.prepareStatement(Mockito.anyString())).thenReturn(preparedStatement);
        Mockito.when(preparedStatement.executeQuery()).thenReturn(resultSet);
        Mockito.when(resultSet.next()).thenReturn(false);

        // Act
        List<TestEntity> results = genericDAO.findAllWithPagination(0, 2);

        // Assert
        Assertions.assertNotNull(results, "Results should not be null");
        Assertions.assertTrue(results.isEmpty(), "Results should be empty for invalid page number");
    }

    @Test
    public void testFindAllWithPagination_InvalidPageSize() throws Exception {
        // Arrange
        Mockito.when(connection.prepareStatement(Mockito.anyString())).thenReturn(preparedStatement);
        Mockito.when(preparedStatement.executeQuery()).thenReturn(resultSet);
        Mockito.when(resultSet.next()).thenReturn(false);

        // Act
        List<TestEntity> results = genericDAO.findAllWithPagination(1, 0);

        // Assert
        Assertions.assertNotNull(results, "Results should not be null");
        Assertions.assertTrue(results.isEmpty(), "Results should be empty for invalid page size");
    }
}