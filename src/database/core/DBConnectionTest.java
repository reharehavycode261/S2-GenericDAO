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

    private DBConnection dbConnection;
    private Database mockDatabase;
    private Connection mockConnection;

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
        Mockito.doNothing().when(mockConnection).commit();

        // Act
        dbConnection.commit();

        // Assert
        Mockito.verify(mockConnection, Mockito.times(1)).commit();
    }

    @Test
    public void testCommitThrowsSQLException() {
        // Arrange
        try {
            Mockito.doThrow(new SQLException("Commit failed")).when(mockConnection).commit();

            // Act & Assert
            Assertions.assertThrows(SQLException.class, () -> dbConnection.commit());
        } catch (SQLException e) {
            // This block will not be executed
        }
    }

    @Test
    public void testRollbackSuccess() {
        // Arrange
        try {
            Mockito.doNothing().when(mockConnection).rollback();

            // Act
            dbConnection.rollback();

            // Assert
            Mockito.verify(mockConnection, Mockito.times(1)).rollback();
        } catch (SQLException e) {
            Assertions.fail("Rollback should not throw SQLException");
        }
    }

    @Test
    public void testRollbackHandlesSQLException() {
        // Arrange
        try {
            Mockito.doThrow(new SQLException("Rollback failed")).when(mockConnection).rollback();

            // Act
            dbConnection.rollback();

            // Assert
            Mockito.verify(mockConnection, Mockito.times(1)).rollback();
        } catch (SQLException e) {
            // This block will not be executed
        }
    }

    @Test
    public void testUpdateFieldsSuccess() throws SQLException {
        // Arrange
        String tableName = "test_table";
        String[] fields = {"field1", "field2"};
        Object[] values = {"value1", "value2"};
        String condition = "id = 1";

        String expectedQuery = "UPDATE test_table SET field1 = ?, field2 = ? WHERE id = 1";
        PreparedStatement mockStatement = Mockito.mock(PreparedStatement.class);
        Mockito.when(mockConnection.prepareStatement(expectedQuery)).thenReturn(mockStatement);

        // Act
        dbConnection.updateFields(tableName, fields, values, condition);

        // Assert
        Mockito.verify(mockConnection, Mockito.times(1)).prepareStatement(expectedQuery);
        Mockito.verify(mockStatement, Mockito.times(1)).setObject(1, "value1");
        Mockito.verify(mockStatement, Mockito.times(1)).setObject(2, "value2");
        Mockito.verify(mockStatement, Mockito.times(1)).executeUpdate();
        Mockito.verify(mockStatement, Mockito.times(1)).close();
    }

    @Test
    public void testUpdateFieldsThrowsSQLException() {
        // Arrange
        String tableName = "test_table";
        String[] fields = {"field1", "field2"};
        Object[] values = {"value1", "value2"};
        String condition = "id = 1";

        try {
            Mockito.when(mockConnection.prepareStatement(Mockito.anyString())).thenThrow(new SQLException("Update failed"));

            // Act & Assert
            Assertions.assertThrows(SQLException.class, () -> dbConnection.updateFields(tableName, fields, values, condition));
        } catch (SQLException e) {
            // This block will not be executed
        }
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
}