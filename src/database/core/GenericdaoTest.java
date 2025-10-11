import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import database.exception.SQL.DatabaseSQLException;
import database.exception.SQL.TableNotFoundException;

@ExtendWith(MockitoExtension.class)
public class GenericDAOTest {

    private GenericDAO dao;
    
    @Mock
    private Database mockDatabase;
    
    @Mock
    private ResultSet mockResultSet;

    @BeforeEach
    void setUp() {
        dao = new GenericDAO();
        dao.setDatabase(mockDatabase);
    }

    @Test
    @DisplayName("Should set and get table name correctly")
    void testTableNameSetterGetter() {
        String tableName = "test_table";
        dao.setTableName(tableName);
        assertEquals(tableName, dao.getTableName());
    }

    @Test
    @DisplayName("Should set and get database correctly")
    void testDatabaseSetterGetter() {
        Database db = mock(Database.class);
        dao.setDatabase(db);
        assertEquals(db, dao.getDatabase());
    }

    @Test
    @DisplayName("Should count records successfully")
    void testCountSuccess() throws SQLException, DatabaseSQLException, TableNotFoundException {
        // Setup
        dao.setTableName("test_table");
        when(mockDatabase.executeQuery(anyString())).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getLong(1)).thenReturn(10L);

        // Execute
        long count = dao.count();

        // Verify
        assertEquals(10L, count);
        verify(mockDatabase).executeQuery("SELECT COUNT(*) FROM test_table");
    }

    @Test
    @DisplayName("Should return 0 when no records found")
    void testCountNoRecords() throws SQLException, DatabaseSQLException, TableNotFoundException {
        // Setup
        dao.setTableName("test_table");
        when(mockDatabase.executeQuery(anyString())).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false);

        // Execute
        long count = dao.count();

        // Verify
        assertEquals(0L, count);
    }

    @Test
    @DisplayName("Should throw TableNotFoundException when table name is null")
    void testCountNullTableName() {
        dao.setTableName(null);
        assertThrows(TableNotFoundException.class, () -> dao.count());
    }

    @Test
    @DisplayName("Should throw TableNotFoundException when table name is empty")
    void testCountEmptyTableName() {
        dao.setTableName("   ");
        assertThrows(TableNotFoundException.class, () -> dao.count());
    }

    @Test
    @DisplayName("Should throw DatabaseSQLException when SQL error occurs")
    void testCountSQLException() throws SQLException {
        // Setup
        dao.setTableName("test_table");
        when(mockDatabase.executeQuery(anyString())).thenThrow(new SQLException("SQL Error"));

        // Verify
        assertThrows(DatabaseSQLException.class, () -> dao.count());
    }

    @Test
    @DisplayName("Should count records with condition successfully")
    void testCountWithConditionSuccess() throws SQLException, DatabaseSQLException, TableNotFoundException {
        // Setup
        dao.setTableName("test_table");
        String condition = "age > 18";
        when(mockDatabase.executeQuery(anyString())).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getLong(1)).thenReturn(5L);

        // Execute
        long count = dao.count(condition);

        // Verify
        assertEquals(5L, count);
        verify(mockDatabase).executeQuery("SELECT COUNT(*) FROM test_table WHERE " + condition);
    }
}