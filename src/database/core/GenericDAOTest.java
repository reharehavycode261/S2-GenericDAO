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
    private final String tableName = "test_table";

    @BeforeEach
    public void setUp() throws SQLException {
        connection = Mockito.mock(Connection.class);
        genericDAO = new GenericDAO<>(connection, tableName);
    }

    @AfterEach
    public void tearDown() throws SQLException {
        connection.close();
    }

    @Test
    public void testUpdateFieldsSuccess() throws SQLException {
        Map<String, Object> fields = new HashMap<>();
        fields.put("column1", "value1");
        fields.put("column2", 123);

        String whereClause = "id = 1";

        PreparedStatement stmt = Mockito.mock(PreparedStatement.class);
        Mockito.when(connection.prepareStatement(Mockito.anyString())).thenReturn(stmt);

        genericDAO.updateFields(fields, whereClause);

        Mockito.verify(stmt, Mockito.times(1)).setObject(1, "value1");
        Mockito.verify(stmt, Mockito.times(1)).setObject(2, 123);
        Mockito.verify(stmt, Mockito.times(1)).executeUpdate();
    }

    @Test
    public void testUpdateFieldsSQLException() throws SQLException {
        Map<String, Object> fields = new HashMap<>();
        fields.put("column1", "value1");

        String whereClause = "id = 1";

        Mockito.when(connection.prepareStatement(Mockito.anyString())).thenThrow(new SQLException());

        Assertions.assertThrows(SQLException.class, () -> {
            genericDAO.updateFields(fields, whereClause);
        });
    }

    @Test
    public void testGetPaginatedResultsSuccess() throws SQLException {
        int pageSize = 10;
        int pageNumber = 1;

        PreparedStatement stmt = Mockito.mock(PreparedStatement.class);
        ResultSet rs = Mockito.mock(ResultSet.class);
        Mockito.when(connection.prepareStatement(Mockito.anyString())).thenReturn(stmt);
        Mockito.when(stmt.executeQuery()).thenReturn(rs);
        Mockito.when(rs.next()).thenReturn(false);

        List<Object> results = genericDAO.getPaginatedResults(pageSize, pageNumber);

        Assertions.assertNotNull(results);
        Assertions.assertTrue(results.isEmpty());
        Mockito.verify(stmt, Mockito.times(1)).setInt(1, pageSize);
        Mockito.verify(stmt, Mockito.times(1)).setInt(2, 0);
    }

    @Test
    public void testGetPaginatedResultsSQLException() throws SQLException {
        int pageSize = 10;
        int pageNumber = 1;

        Mockito.when(connection.prepareStatement(Mockito.anyString())).thenThrow(new SQLException());

        Assertions.assertThrows(SQLException.class, () -> {
            genericDAO.getPaginatedResults(pageSize, pageNumber);
        });
    }

    // Mock method to simulate parsing a ResultSet row into an object of type T
    private Object parseRow(ResultSet rs) {
        // Mock implementation
        return new Object();
    }
}