package test.database.core;

import database.core.DBConnection;
import org.junit.jupiter.api.*;
import org.mockito.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.mockito.Mockito.*;

public class DBConnectionTest {

    @Mock
    Connection mockConnection;

    @Mock
    PreparedStatement mockStatement;

    DBConnection dbConnection;

    @BeforeEach
    public void setup() throws SQLException {
        MockitoAnnotations.openMocks(this);
        dbConnection = new DBConnection(null, mockConnection);

        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
    }

    @Test
    public void testUpdateFieldsSuccess() throws SQLException {
        String[] fields = {"name", "age"};
        Object[] values = {"John Doe", 30};
        String condition = "id = 1";

        dbConnection.updateFields("users", fields, values, condition);

        verify(mockStatement).setObject(1, "John Doe");
        verify(mockStatement).setObject(2, 30);
        verify(mockStatement).executeUpdate();
    }

    @Test
    public void testUpdateFieldsIllegalArgumentException() {
        String[] fields = {"name", "age"};
        Object[] values = {"John Doe"};
        String condition = "id = 1";

        Assertions.assertThrows(IllegalArgumentException.class, () ->
                dbConnection.updateFields("users", fields, values, condition));
    }

    @Test
    public void testUpdateFieldsSQLExecution() throws SQLException {
        String[] fields = {"name", "age"};
        Object[] values = {"John Doe", 30};
        String condition = "id = 1";

        doThrow(new SQLException()).when(mockStatement).executeUpdate();

        Assertions.assertThrows(SQLException.class, () ->
                dbConnection.updateFields("users", fields, values, condition));
    }
}