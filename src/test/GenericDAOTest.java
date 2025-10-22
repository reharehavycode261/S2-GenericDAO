package test;

import database.core.DBConnection;
import database.core.GenericDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class GenericDAOTest {

    private DBConnection dbConnection;
    private Connection connection;
    private GenericDAO<Object> genericDAO;

    @BeforeEach
    public void setUp() throws SQLException {
        connection = mock(Connection.class);
        dbConnection = new DBConnection(null, connection);
        genericDAO = new GenericDAO<>(dbConnection, "test_table");
    }

    @Test
    public void testUpdateFieldsSuccessful() throws SQLException {
        String[] fields = {"name", "age"};
        Object[] values = {"John Doe", 30};
        String condition = "id = 1";

        PreparedStatement preparedStatement = mock(PreparedStatement.class);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);

        genericDAO.updateFields(fields, values, condition);

        verify(preparedStatement, times(1)).executeUpdate();
        verify(preparedStatement).setObject(1, "John Doe");
        verify(preparedStatement).setObject(2, 30);
    }

    @Test
    public void testUpdateFieldsThrowsIllegalArgumentException() {
        String[] fields = {"name"};
        Object[] values = {"John Doe", 30};

        assertThrows(IllegalArgumentException.class, () -> {
            genericDAO.updateFields(fields, values, "id = 1");
        });
    }

    @Test
    public void testUpdateFieldsWithEmptyFields() {
        String[] fields = {};
        Object[] values = {};

        assertThrows(IllegalArgumentException.class, () -> {
            genericDAO.updateFields(fields, values, "id = 1");
        });
    }
}