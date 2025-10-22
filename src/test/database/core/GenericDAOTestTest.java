package test.database.core;

import database.core.GenericDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class GenericDAOTest {

    @Mock
    Connection mockConnection;

    @Mock
    PreparedStatement mockPreparedStatement;

    private GenericDAO<Object> genericDAO;

    @BeforeEach
    public void setup() throws SQLException {
        MockitoAnnotations.openMocks(this);
        genericDAO = new GenericDAO<>();
        genericDAO.tableName = "my_table"; // Assuming the table name is set here

        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
    }

    /**
     * Test the successful update of fields in the database.
     * This test verifies that the correct SQL statement is prepared and executed.
     */
    @Test
    public void testUpdateFields_success() throws SQLException {
        String[] fields = {"name", "age"};
        Object[] values = {"John Doe", 30};
        String condition = "id = 1";

        genericDAO.updateFields(mockConnection, fields, values, condition);

        verify(mockConnection).prepareStatement("UPDATE my_table SET name = ?, age = ? WHERE id = 1");
        verify(mockPreparedStatement).setObject(1, "John Doe");
        verify(mockPreparedStatement).setObject(2, 30);
        verify(mockPreparedStatement).executeUpdate();
    }

    /**
     * Test the updateFields method when the lengths of fields and values do not match.
     * This should throw an IllegalArgumentException.
     */
    @Test
    public void testUpdateFields_mismatchedLength() {
        String[] fields = {"name"};
        Object[] values = {"John Doe", 30};

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            genericDAO.updateFields(mockConnection, fields, values, "id = 1");
        });

        assertEquals("Le nombre de champs doit être égal au nombre de valeurs", exception.getMessage());
    }

    /**
     * Test the updateFields method when the SQL execution fails.
     * This should propagate the SQLException.
     */
    @Test
    public void testUpdateFields_sqlException() throws SQLException {
        String[] fields = {"name", "age"};
        Object[] values = {"John Doe", 30};
        String condition = "id = 1";

        when(mockPreparedStatement.executeUpdate()).thenThrow(new SQLException("SQL Error"));

        SQLException exception = assertThrows(SQLException.class, () -> {
            genericDAO.updateFields(mockConnection, fields, values, condition);
        });

        assertEquals("SQL Error", exception.getMessage());
    }

    /**
     * Test the updateFields method with an empty condition.
     * This should still execute the update without a WHERE clause.
     */
    @Test
    public void testUpdateFields_emptyCondition() throws SQLException {
        String[] fields = {"name", "age"};
        Object[] values = {"John Doe", 30};
        String condition = ""; // No condition

        genericDAO.updateFields(mockConnection, fields, values, condition);

        verify(mockConnection).prepareStatement("UPDATE my_table SET name = ?, age = ?");
        verify(mockPreparedStatement).setObject(1, "John Doe");
        verify(mockPreparedStatement).setObject(2, 30);
        verify(mockPreparedStatement).executeUpdate();
    }
}