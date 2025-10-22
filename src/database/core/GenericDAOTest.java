import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.mockito.Mockito;
import java.sql.*;
import java.util.*;

public class GenericDAOTest {

    private Connection connection;
    private GenericDAO<Object> genericDAO;

    @BeforeEach
    public void setUp() throws SQLException {
        connection = Mockito.mock(Connection.class);
        genericDAO = new GenericDAO<>(connection, "test_table");
    }

    @AfterEach
    public void tearDown() throws SQLException {
        connection.close();
    }

    @Test
    public void testUpdateFieldsSuccess() throws SQLException {
        // Arrange
        PreparedStatement stmt = Mockito.mock(PreparedStatement.class);
        Mockito.when(connection.prepareStatement(Mockito.anyString())).thenReturn(stmt);
        Mockito.when(stmt.executeUpdate()).thenReturn(1);

        Map<String, Object> fields = new HashMap<>();
        fields.put("name", "John Doe");
        fields.put("age", 30);

        // Act
        boolean result = genericDAO.updateFields(1, fields);

        // Assert
        Assertions.assertTrue(result, "The update should be successful.");
        Mockito.verify(stmt, Mockito.times(1)).executeUpdate();
    }

    @Test
    public void testUpdateFieldsWithEmptyFields() {
        // Arrange
        Map<String, Object> fields = new HashMap<>();

        // Act & Assert
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            genericDAO.updateFields(1, fields);
        }, "An empty fields map should throw an IllegalArgumentException.");
    }

    @Test
    public void testUpdateFieldsWithNullFields() {
        // Act & Assert
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            genericDAO.updateFields(1, null);
        }, "A null fields map should throw an IllegalArgumentException.");
    }

    @Test
    public void testFindWithPaginationSuccess() throws SQLException {
        // Arrange
        PreparedStatement stmt = Mockito.mock(PreparedStatement.class);
        ResultSet rs = Mockito.mock(ResultSet.class);
        Mockito.when(connection.prepareStatement(Mockito.anyString())).thenReturn(stmt);
        Mockito.when(stmt.executeQuery()).thenReturn(rs);
        Mockito.when(rs.next()).thenReturn(true, false);

        // Mock the mapping method
        GenericDAO<Object> spyDAO = Mockito.spy(genericDAO);
        Mockito.doReturn(new Object()).when(spyDAO).mapResultSetToEntity(rs);

        // Act
        List<Object> results = spyDAO.findWithPagination(10, 0);

        // Assert
        Assertions.assertNotNull(results, "The results list should not be null.");
        Assertions.assertEquals(1, results.size(), "The results list should contain one element.");
        Mockito.verify(stmt, Mockito.times(1)).executeQuery();
        Mockito.verify(rs, Mockito.times(2)).next();
    }

    @Test
    public void testFindWithPaginationNoResults() throws SQLException {
        // Arrange
        PreparedStatement stmt = Mockito.mock(PreparedStatement.class);
        ResultSet rs = Mockito.mock(ResultSet.class);
        Mockito.when(connection.prepareStatement(Mockito.anyString())).thenReturn(stmt);
        Mockito.when(stmt.executeQuery()).thenReturn(rs);
        Mockito.when(rs.next()).thenReturn(false);

        // Act
        List<Object> results = genericDAO.findWithPagination(10, 0);

        // Assert
        Assertions.assertNotNull(results, "The results list should not be null.");
        Assertions.assertTrue(results.isEmpty(), "The results list should be empty.");
        Mockito.verify(stmt, Mockito.times(1)).executeQuery();
        Mockito.verify(rs, Mockito.times(1)).next();
    }

    @Test
    public void testFindWithPaginationSQLException() throws SQLException {
        // Arrange
        Mockito.when(connection.prepareStatement(Mockito.anyString())).thenThrow(new SQLException());

        // Act & Assert
        Assertions.assertThrows(SQLException.class, () -> {
            genericDAO.findWithPagination(10, 0);
        }, "A SQLException should be thrown if the query fails.");
    }
}