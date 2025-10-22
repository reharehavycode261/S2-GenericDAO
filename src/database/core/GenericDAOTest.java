import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.mockito.Mockito;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import database.core.DBConnection;
import database.core.GenericDAO;

class GenericDAOTest {

    private DBConnection mockDbConnection;
    private Connection mockConnection;
    private PreparedStatement mockPreparedStatement;
    private GenericDAO<Object> genericDAO;

    @BeforeEach
    void setUp() throws SQLException {
        mockDbConnection = Mockito.mock(DBConnection.class);
        mockConnection = Mockito.mock(Connection.class);
        mockPreparedStatement = Mockito.mock(PreparedStatement.class);

        Mockito.when(mockDbConnection.getConnection()).thenReturn(mockConnection);
        Mockito.when(mockConnection.prepareStatement(Mockito.anyString())).thenReturn(mockPreparedStatement);

        genericDAO = new GenericDAO<>(mockDbConnection, "test_table");
    }

    @AfterEach
    void tearDown() {
        Mockito.reset(mockDbConnection, mockConnection, mockPreparedStatement);
    }

    @Test
    void testUpdateFieldsSuccess() throws SQLException {
        String[] fields = {"name", "age"};
        Object[] values = {"John Doe", 30};
        String condition = "id = 1";

        genericDAO.updateFields(fields, values, condition);

        Mockito.verify(mockConnection).prepareStatement("UPDATE test_table SET name = ?, age = ? WHERE id = 1");
        Mockito.verify(mockPreparedStatement).setObject(1, "John Doe");
        Mockito.verify(mockPreparedStatement).setObject(2, 30);
        Mockito.verify(mockPreparedStatement).executeUpdate();
    }

    @Test
    void testUpdateFieldsEmptyFields() {
        String[] fields = {};
        Object[] values = {};
        String condition = "id = 1";

        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            genericDAO.updateFields(fields, values, condition);
        });

        Assertions.assertEquals("Fields and values must have the same non-zero length.", exception.getMessage());
    }

    @Test
    void testUpdateFieldsMismatchedFieldsAndValues() {
        String[] fields = {"name"};
        Object[] values = {"John Doe", 30};
        String condition = "id = 1";

        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            genericDAO.updateFields(fields, values, condition);
        });

        Assertions.assertEquals("Fields and values must have the same non-zero length.", exception.getMessage());
    }

    @Test
    void testUpdateFieldsSQLException() throws SQLException {
        String[] fields = {"name"};
        Object[] values = {"John Doe"};
        String condition = "id = 1";

        Mockito.when(mockConnection.prepareStatement(Mockito.anyString())).thenThrow(new SQLException("SQL error"));

        SQLException exception = Assertions.assertThrows(SQLException.class, () -> {
            genericDAO.updateFields(fields, values, condition);
        });

        Assertions.assertEquals("SQL error", exception.getMessage());
    }
}