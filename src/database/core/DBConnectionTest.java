package database.core;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.mockito.Mockito;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DBConnectionTest {

    private Database mockDatabase;
    private Connection mockConnection;
    private DBConnection dbConnection;

    @BeforeEach
    public void setUp() {
        mockDatabase = Mockito.mock(Database.class);
        mockConnection = Mockito.mock(Connection.class);
        dbConnection = new DBConnection(mockDatabase, mockConnection);
    }

    @AfterEach
    public void tearDown() {
        mockDatabase = null;
        mockConnection = null;
        dbConnection = null;
    }

    @Test
    public void testCommitSuccess() throws SQLException {
        // Test successful commit
        dbConnection.commit();
        Mockito.verify(mockConnection).commit();
    }

    @Test
    public void testCommitThrowsSQLException() throws SQLException {
        // Test commit throws SQLException
        Mockito.doThrow(new SQLException("Commit failed")).when(mockConnection).commit();
        SQLException exception = Assertions.assertThrows(SQLException.class, () -> {
            dbConnection.commit();
        });
        Assertions.assertEquals("Commit failed", exception.getMessage());
    }

    @Test
    public void testRollbackSuccess() throws SQLException {
        // Test successful rollback
        dbConnection.rollback();
        Mockito.verify(mockConnection).rollback();
    }

    @Test
    public void testRollbackPrintsSQLException() throws SQLException {
        // Test rollback prints SQLException
        Mockito.doThrow(new SQLException("Rollback failed")).when(mockConnection).rollback();
        dbConnection.rollback();
        Mockito.verify(mockConnection).rollback();
    }

    @Test
    public void testUpdateFieldsSuccess() throws SQLException {
        // Test successful updateFields
        String tableName = "test_table";
        String[] fields = {"field1", "field2"};
        Object[] values = {"value1", "value2"};
        String condition = "id = 1";

        PreparedStatement mockPreparedStatement = Mockito.mock(PreparedStatement.class);
        Mockito.when(mockConnection.prepareStatement(Mockito.anyString())).thenReturn(mockPreparedStatement);

        dbConnection.updateFields(tableName, fields, values, condition);

        Mockito.verify(mockConnection).prepareStatement("UPDATE test_table SET field1 = ?, field2 = ? WHERE id = 1");
        Mockito.verify(mockPreparedStatement).setObject(1, "value1");
        Mockito.verify(mockPreparedStatement).setObject(2, "value2");
        Mockito.verify(mockPreparedStatement).executeUpdate();
        Mockito.verify(mockPreparedStatement).close();
    }

    @Test
    public void testUpdateFieldsThrowsSQLException() throws SQLException {
        // Test updateFields throws SQLException
        String tableName = "test_table";
        String[] fields = {"field1"};
        Object[] values = {"value1"};
        String condition = "id = 1";

        Mockito.when(mockConnection.prepareStatement(Mockito.anyString())).thenThrow(new SQLException("Update failed"));

        SQLException exception = Assertions.assertThrows(SQLException.class, () -> {
            dbConnection.updateFields(tableName, fields, values, condition);
        });
        Assertions.assertEquals("Update failed", exception.getMessage());
    }

    @Test
    public void testUpdateFieldsThrowsIllegalArgumentException() {
        // Test updateFields throws IllegalArgumentException for mismatched fields and values length
        String tableName = "test_table";
        String[] fields = {"field1"};
        Object[] values = {"value1", "value2"};
        String condition = "id = 1";

        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            dbConnection.updateFields(tableName, fields, values, condition);
        });
        Assertions.assertEquals("La longueur des champs et des valeurs doit correspondre.", exception.getMessage());
    }
}