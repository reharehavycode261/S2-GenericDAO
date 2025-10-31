import database.core.GenericDAO;
import database.core.DBConnection;
import database.exception.SQL.AttributeMissingException;
import database.exception.SQL.AttributeTypeNotExistingException;
import database.exception.object.NotIdentifiedInDatabaseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class GenericDAOTest {

    private GenericDAO genericDAO;
    private DBConnection mockDBConnection;

    @BeforeEach
    public void setUp() {
        genericDAO = new GenericDAO();
        mockDBConnection = Mockito.mock(DBConnection.class);
    }

    @Test
    public void testGetDataWithCacheHit() throws SQLException {
        String query = "SELECT * FROM table";
        List<Object> expectedData = Arrays.asList("data1", "data2");
        Mockito.when(mockDBConnection.getCachedResult("GenericDAO_" + query)).thenReturn(expectedData);

        List<Object> result = genericDAO.getData(mockDBConnection, query);

        assertEquals(expectedData, result, "Data should be retrieved from cache");
        Mockito.verify(mockDBConnection, Mockito.never()).setCachedResult(Mockito.anyString(), Mockito.anyList());
    }

    @Test
    public void testGetDataWithCacheMiss() throws SQLException {
        String query = "SELECT * FROM table";
        List<Object> expectedData = Arrays.asList("data1", "data2");
        Mockito.when(mockDBConnection.getCachedResult("GenericDAO_" + query)).thenReturn(null);
        Mockito.when(mockDBConnection.getQueryResult(query)).thenReturn(expectedData);

        List<Object> result = genericDAO.getData(mockDBConnection, query);

        assertEquals(expectedData, result, "Data should be retrieved from database and cached");
        Mockito.verify(mockDBConnection).setCachedResult("GenericDAO_" + query, expectedData);
    }

    @Test
    public void testCreate() throws SQLException {
        Object data = new Object();
        genericDAO.create(mockDBConnection, data);

        Mockito.verify(mockDBConnection).clearAllCache();
    }

    @Test
    public void testUpdate() throws SQLException {
        Object data = new Object();
        genericDAO.update(mockDBConnection, data);

        Mockito.verify(mockDBConnection).clearAllCache();
    }

    @Test
    public void testDelete() throws SQLException {
        Object data = new Object();
        genericDAO.delete(mockDBConnection, data);

        Mockito.verify(mockDBConnection).clearAllCache();
    }

    @Test
    public void testGetDataThrowsSQLException() throws SQLException {
        String query = "SELECT * FROM table";
        Mockito.when(mockDBConnection.getCachedResult("GenericDAO_" + query)).thenThrow(new SQLException("Database error"));

        SQLException exception = assertThrows(SQLException.class, () -> {
            genericDAO.getData(mockDBConnection, query);
        });

        assertEquals("Database error", exception.getMessage(), "Exception message should match");
    }

    @Test
    public void testCreateThrowsSQLException() throws SQLException {
        Object data = new Object();
        Mockito.doThrow(new SQLException("Insert error")).when(mockDBConnection).clearAllCache();

        SQLException exception = assertThrows(SQLException.class, () -> {
            genericDAO.create(mockDBConnection, data);
        });

        assertEquals("Insert error", exception.getMessage(), "Exception message should match");
    }

    @Test
    public void testUpdateThrowsSQLException() throws SQLException {
        Object data = new Object();
        Mockito.doThrow(new SQLException("Update error")).when(mockDBConnection).clearAllCache();

        SQLException exception = assertThrows(SQLException.class, () -> {
            genericDAO.update(mockDBConnection, data);
        });

        assertEquals("Update error", exception.getMessage(), "Exception message should match");
    }

    @Test
    public void testDeleteThrowsSQLException() throws SQLException {
        Object data = new Object();
        Mockito.doThrow(new SQLException("Delete error")).when(mockDBConnection).clearAllCache();

        SQLException exception = assertThrows(SQLException.class, () -> {
            genericDAO.delete(mockDBConnection, data);
        });

        assertEquals("Delete error", exception.getMessage(), "Exception message should match");
    }
}