package database.core;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.mockito.Mockito;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.mockito.Mockito.*;

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
        dbConnection = null;
    }

    @Test
    public void testCommitSuccess() throws SQLException {
        // Arrange
        doNothing().when(mockConnection).commit();

        // Act
        dbConnection.commit();

        // Assert
        verify(mockConnection, times(1)).commit();
    }

    @Test
    public void testCommitThrowsSQLException() {
        // Arrange
        try {
            doThrow(new SQLException("Commit failed")).when(mockConnection).commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Act & Assert
        Assertions.assertThrows(SQLException.class, () -> dbConnection.commit());
    }

    @Test
    public void testRollbackSuccess() {
        // Arrange
        try {
            doNothing().when(mockConnection).rollback();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Act
        dbConnection.rollback();

        // Assert
        try {
            verify(mockConnection, times(1)).rollback();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testRollbackSQLException() {
        // Arrange
        try {
            doThrow(new SQLException("Rollback failed")).when(mockConnection).rollback();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Act
        dbConnection.rollback();

        // Assert
        try {
            verify(mockConnection, times(1)).rollback();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testUpdateFieldsSuccess() throws SQLException {
        // Arrange
        String tableName = "test_table";
        String[] fields = {"field1", "field2"};
        Object[] values = {"value1", "value2"};
        String condition = "id = 1";

        PreparedStatement mockPreparedStatement = Mockito.mock(PreparedStatement.class);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);

        // Act
        dbConnection.updateFields(tableName, fields, values, condition);

        // Assert
        verify(mockConnection, times(1)).prepareStatement(anyString());
        verify(mockPreparedStatement, times(1)).setObject(1, "value1");
        verify(mockPreparedStatement, times(1)).setObject(2, "value2");
        verify(mockPreparedStatement, times(1)).executeUpdate();
        verify(mockPreparedStatement, times(1)).close();
    }

    @Test
    public void testUpdateFieldsThrowsSQLException() throws SQLException {
        // Arrange
        String tableName = "test_table";
        String[] fields = {"field1", "field2"};
        Object[] values = {"value1", "value2"};
        String condition = "id = 1";

        when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("Update failed"));

        // Act & Assert
        Assertions.assertThrows(SQLException.class, () -> dbConnection.updateFields(tableName, fields, values, condition));
    }

    @Test
    public void testUpdateFieldsMismatchedFieldsAndValues() {
        // Arrange
        String tableName = "test_table";
        String[] fields = {"field1"};
        Object[] values = {"value1", "value2"};
        String condition = "id = 1";

        // Act & Assert
        Assertions.assertThrows(IllegalArgumentException.class, () -> dbConnection.updateFields(tableName, fields, values, condition));
    }

    @Test
    public void testGetDatabase() {
        // Act
        Database result = dbConnection.getDatabase();

        // Assert
        Assertions.assertEquals(mockDatabase, result, "The database should be the same as the one set in the constructor.");
    }
}