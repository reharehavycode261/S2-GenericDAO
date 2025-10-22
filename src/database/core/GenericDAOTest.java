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

public class GenericDAOTest {

    private Connection mockConnection;
    private PreparedStatement mockPreparedStatement;
    private GenericDAO<Object> genericDAO;
    private DBConnection mockDBConnection;

    @BeforeEach
    public void setUp() throws SQLException {
        mockConnection = mock(Connection.class);
        mockPreparedStatement = mock(PreparedStatement.class);
        mockDBConnection = mock(DBConnection.class);

        when(mockDBConnection.getConnection()).thenReturn(mockConnection);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);

        genericDAO = new GenericDAO<>(mockConnection, "test_table");
    }

    @AfterEach
    public void tearDown() {
        mockConnection = null;
        mockPreparedStatement = null;
        genericDAO = null;
        mockDBConnection = null;
    }

    @Test
    public void testUpdateFieldsSuccess() throws SQLException {
        String[] fields = {"field1", "field2"};
        Object[] values = {123, "value"};
        String condition = "id = 1";

        genericDAO.updateFields(mockDBConnection, fields, values, condition);

        verify(mockConnection).prepareStatement("UPDATE test_table SET field1 = ?, field2 = ? WHERE id = 1");
        verify(mockPreparedStatement).setObject(1, 123);
        verify(mockPreparedStatement).setObject(2, "value");
        verify(mockPreparedStatement).executeUpdate();
    }

    @Test
    public void testUpdateFieldsWithNullFields() {
        String[] fields = null;
        Object[] values = {123, "value"};
        String condition = "id = 1";

        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            genericDAO.updateFields(mockDBConnection, fields, values, condition);
        });

        Assertions.assertEquals("Les champs et les valeurs doivent être non nuls et de la même longueur", exception.getMessage());
    }

    @Test
    public void testUpdateFieldsWithNullValues() {
        String[] fields = {"field1", "field2"};
        Object[] values = null;
        String condition = "id = 1";

        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            genericDAO.updateFields(mockDBConnection, fields, values, condition);
        });

        Assertions.assertEquals("Les champs et les valeurs doivent être non nuls et de la même longueur", exception.getMessage());
    }

    @Test
    public void testUpdateFieldsWithMismatchedFieldsAndValues() {
        String[] fields = {"field1"};
        Object[] values = {123, "value"};
        String condition = "id = 1";

        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            genericDAO.updateFields(mockDBConnection, fields, values, condition);
        });

        Assertions.assertEquals("Les champs et les valeurs doivent être non nuls et de la même longueur", exception.getMessage());
    }

    @Test
    public void testUpdateFieldsSQLException() throws SQLException {
        String[] fields = {"field1", "field2"};
        Object[] values = {123, "value"};
        String condition = "id = 1";

        when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("SQL Error"));

        SQLException exception = Assertions.assertThrows(SQLException.class, () -> {
            genericDAO.updateFields(mockDBConnection, fields, values, condition);
        });

        Assertions.assertEquals("SQL Error", exception.getMessage());
    }
}