package database.core;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

public class GenericDAOTest {

    private GenericDAO<Object> genericDAO;
    private DBConnection mockDbConnection;
    private Connection mockConnection;
    private PreparedStatement mockPreparedStatement;

    @BeforeEach
    public void setUp() throws SQLException {
        genericDAO = new GenericDAO<>();
        mockDbConnection = mock(DBConnection.class);
        mockConnection = mock(Connection.class);
        mockPreparedStatement = mock(PreparedStatement.class);

        when(mockDbConnection.getConnection()).thenReturn(mockConnection);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
    }

    @Test
    public void testUpdateFieldsSuccess() throws SQLException {
        String[] fields = {"field1", "field2"};
        Object[] values = {"value1", 123};
        String condition = "id = 1";

        genericDAO.updateFields(mockDbConnection, fields, values, condition);

        ArgumentCaptor<String> sqlCaptor = ArgumentCaptor.forClass(String.class);
        verify(mockConnection).prepareStatement(sqlCaptor.capture());
        String expectedSql = "UPDATE your_table_name SET field1 = ?, field2 = ? WHERE id = 1";
        assertEquals(expectedSql, sqlCaptor.getValue(), "SQL query should match the expected format.");

        verify(mockPreparedStatement).setObject(1, "value1");
        verify(mockPreparedStatement).setObject(2, 123);
        verify(mockPreparedStatement).executeUpdate();
    }

    @Test
    public void testUpdateFieldsMismatchFieldsAndValues() {
        String[] fields = {"field1"};
        Object[] values = {"value1", 123};
        String condition = "id = 1";

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            genericDAO.updateFields(mockDbConnection, fields, values, condition);
        });

        assertEquals("Le nombre de champs et de valeurs doit être le même.", exception.getMessage(),
                "Exception message should indicate mismatch between fields and values.");
    }

    @Test
    public void testUpdateFieldsSQLException() throws SQLException {
        String[] fields = {"field1", "field2"};
        Object[] values = {"value1", 123};
        String condition = "id = 1";

        when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("Database error"));

        SQLException exception = assertThrows(SQLException.class, () -> {
            genericDAO.updateFields(mockDbConnection, fields, values, condition);
        });

        assertEquals("Database error", exception.getMessage(), "Exception message should match the SQL error.");
    }

    @Test
    public void testUpdateFieldsWithEmptyFieldsAndValues() throws SQLException {
        String[] fields = {};
        Object[] values = {};
        String condition = "id = 1";

        genericDAO.updateFields(mockDbConnection, fields, values, condition);

        ArgumentCaptor<String> sqlCaptor = ArgumentCaptor.forClass(String.class);
        verify(mockConnection).prepareStatement(sqlCaptor.capture());
        String expectedSql = "UPDATE your_table_name SET  WHERE id = 1";
        assertEquals(expectedSql, sqlCaptor.getValue(), "SQL query should handle empty fields and values correctly.");

        verify(mockPreparedStatement).executeUpdate();
    }
}