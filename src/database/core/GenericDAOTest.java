package database.core;

import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class GenericDAOTest {

    private GenericDAO<Object> genericDAO;

    @Mock
    private DBConnection dbConnectionMock;

    @Mock
    private Connection connectionMock;

    @Mock
    private PreparedStatement preparedStatementMock;

    @BeforeEach
    public void setUp() throws SQLException {
        MockitoAnnotations.openMocks(this);
        genericDAO = new GenericDAO<>("test_table");
        when(dbConnectionMock.getConnection()).thenReturn(connectionMock);
        when(connectionMock.prepareStatement(anyString())).thenReturn(preparedStatementMock);
    }

    @Test
    public void testUpdateFields_Success() throws SQLException {
        String[] fields = {"field1", "field2"};
        Object[] values = {"value1", 123};
        String condition = "id = 1";

        genericDAO.updateFields(dbConnectionMock, fields, values, condition);

        verify(connectionMock).prepareStatement("UPDATE test_table SET field1 = ?, field2 = ? WHERE id = 1");
        verify(preparedStatementMock).setObject(1, "value1");
        verify(preparedStatementMock).setObject(2, 123);
        verify(preparedStatementMock).executeUpdate();
    }

    @Test
    public void testUpdateFields_ThrowsIllegalArgumentException_WhenFieldsAndValuesLengthMismatch() {
        String[] fields = {"field1"};
        Object[] values = {"value1", 123};
        String condition = "id = 1";

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            genericDAO.updateFields(dbConnectionMock, fields, values, condition);
        });
    }

    @Test
    public void testUpdateFields_ThrowsSQLException_WhenSQLExecutionFails() throws SQLException {
        String[] fields = {"field1", "field2"};
        Object[] values = {"value1", 123};
        String condition = "id = 1";

        doThrow(new SQLException("SQL error")).when(preparedStatementMock).executeUpdate();

        Assertions.assertThrows(SQLException.class, () -> {
            genericDAO.updateFields(dbConnectionMock, fields, values, condition);
        });
    }

    @Test
    public void testUpdateFields_ThrowsSQLException_WhenConnectionFails() throws SQLException {
        String[] fields = {"field1", "field2"};
        Object[] values = {"value1", 123};
        String condition = "id = 1";

        when(dbConnectionMock.getConnection()).thenThrow(new SQLException("Connection error"));

        Assertions.assertThrows(SQLException.class, () -> {
            genericDAO.updateFields(dbConnectionMock, fields, values, condition);
        });
    }
}