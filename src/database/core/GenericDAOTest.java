package database.core;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.mockito.Mockito;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

class GenericDAOTest {

    private Connection mockConnection;
    private PreparedStatement mockPreparedStatement;
    private ResultSet mockResultSet;
    private GenericDAO<Object> genericDAO;

    @BeforeEach
    void setUp() throws SQLException {
        mockConnection = Mockito.mock(Connection.class);
        mockPreparedStatement = Mockito.mock(PreparedStatement.class);
        mockResultSet = Mockito.mock(ResultSet.class);

        genericDAO = new GenericDAO<Object>() {
            {
                this.connection = mockConnection;
                this.tableName = "test_table";
            }
        };

        Mockito.when(mockConnection.prepareStatement(Mockito.anyString())).thenReturn(mockPreparedStatement);
        Mockito.when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
    }

    @AfterEach
    void tearDown() throws SQLException {
        Mockito.verifyNoMoreInteractions(mockConnection, mockPreparedStatement, mockResultSet);
    }

    @Test
    void testUpdateFieldsSuccess() throws SQLException {
        Map<String, Object> fieldsToUpdate = new HashMap<>();
        fieldsToUpdate.put("name", "John Doe");
        fieldsToUpdate.put("age", 30);

        Mockito.when(mockPreparedStatement.executeUpdate()).thenReturn(1);

        boolean result = genericDAO.updateFields(1, fieldsToUpdate);

        Assertions.assertTrue(result, "The update should be successful.");
        Mockito.verify(mockPreparedStatement, Mockito.times(1)).executeUpdate();
    }

    @Test
    void testUpdateFieldsEmptyFields() {
        Map<String, Object> fieldsToUpdate = new HashMap<>();

        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            genericDAO.updateFields(1, fieldsToUpdate);
        });

        Assertions.assertEquals("fieldsToUpdate ne peut pas être vide", exception.getMessage());
    }

    @Test
    void testUpdateFieldsSQLException() throws SQLException {
        Map<String, Object> fieldsToUpdate = new HashMap<>();
        fieldsToUpdate.put("name", "John Doe");

        Mockito.when(mockPreparedStatement.executeUpdate()).thenThrow(new SQLException("Database error"));

        SQLException exception = Assertions.assertThrows(SQLException.class, () -> {
            genericDAO.updateFields(1, fieldsToUpdate);
        });

        Assertions.assertEquals("Database error", exception.getMessage());
    }

    @Test
    void testGetPagedResultsSuccess() throws SQLException {
        Mockito.when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);

        ResultSet resultSet = genericDAO.getPagedResults(1, 10);

        Assertions.assertNotNull(resultSet, "ResultSet should not be null.");
        Mockito.verify(mockPreparedStatement, Mockito.times(1)).executeQuery();
    }

    @Test
    void testGetPagedResultsInvalidPageNumber() {
        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            genericDAO.getPagedResults(0, 10);
        });

        Assertions.assertEquals("pageNumber et pageSize doivent être positifs", exception.getMessage());
    }

    @Test
    void testGetPagedResultsInvalidPageSize() {
        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            genericDAO.getPagedResults(1, 0);
        });

        Assertions.assertEquals("pageNumber et pageSize doivent être positifs", exception.getMessage());
    }

    @Test
    void testGetPagedResultsSQLException() throws SQLException {
        Mockito.when(mockPreparedStatement.executeQuery()).thenThrow(new SQLException("Database error"));

        SQLException exception = Assertions.assertThrows(SQLException.class, () -> {
            genericDAO.getPagedResults(1, 10);
        });

        Assertions.assertEquals("Database error", exception.getMessage());
    }
}