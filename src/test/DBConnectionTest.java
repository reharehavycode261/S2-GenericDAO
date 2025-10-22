import database.core.DBConnection;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.mockito.Mockito.*;

public class DBConnectionTest {

    private DBConnection dbConnection;
    private Connection mockConnection;

    @Before
    public void setUp() throws SQLException {
        mockConnection = mock(Connection.class);
        dbConnection = new DBConnection(null, mockConnection);
    }

    @Test
    public void testUpdateFields() throws SQLException {
        String[] fields = {"name", "email"};
        Object[] values = {"John Doe", "john.doe@example.com"};
        String condition = "id = 1";

        dbConnection.updateFields("users", fields, values, condition);

        verify(mockConnection.prepareStatement(
                "UPDATE users SET name = ?, email = ? WHERE id = 1"
        )).executeUpdate();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateFieldsFieldsValuesMismatch() throws SQLException {
        String[] fields = {"name"};
        Object[] values = {"John Doe", "john.doe@example.com"};
        String condition = "id = 1";

        dbConnection.updateFields("users", fields, values, condition);
    }
}