import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.mockito.Mockito;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class GenericDAOTest {

    private GenericDAO<Object> genericDAO;
    private DBConnection mockDbConnection;
    private Connection mockConnection;
    private PreparedStatement mockPreparedStatement;

    @BeforeEach
    public void setUp() throws SQLException {
        genericDAO = new GenericDAO<>();
        mockDbConnection = Mockito.mock(DBConnection.class);
        mockConnection = Mockito.mock(Connection.class);
        mockPreparedStatement = Mockito.mock(PreparedStatement.class);

        Mockito.when(mockDbConnection.getConnection()).thenReturn(mockConnection);
        Mockito.when(mockConnection.prepareStatement(Mockito.anyString())).thenReturn(mockPreparedStatement);
    }

    @AfterEach
    public void tearDown() throws SQLException {
        Mockito.verifyNoMoreInteractions(mockDbConnection, mockConnection, mockPreparedStatement);
    }

    @Test
    public void testUpdateFields_Success() throws SQLException {
        String[] fields = {"field1", "field2"};
        Object[] values = {"value1", 123};
        String condition = "id = 1";

        genericDAO.updateFields(mockDbConnection, fields, values, condition);

        Mockito.verify(mockDbConnection).getConnection();
        Mockito.verify(mockConnection).prepareStatement("UPDATE table_name SET field1 = ?, field2 = ? WHERE id = 1");
        Mockito.verify(mockPreparedStatement).setObject(1, "value1");
        Mockito.verify(mockPreparedStatement).setObject(2, 123);
        Mockito.verify(mockPreparedStatement).executeUpdate();
        Mockito.verify(mockPreparedStatement).close();
        Mockito.verify(mockConnection).close();
    }

    @Test
    public void testUpdateFields_ThrowsIllegalArgumentException_WhenFieldsAndValuesLengthMismatch() {
        String[] fields = {"field1"};
        Object[] values = {"value1", 123};
        String condition = "id = 1";

        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            genericDAO.updateFields(mockDbConnection, fields, values, condition);
        });

        Assertions.assertEquals("Le nombre de champs et de valeurs doit être le même", exception.getMessage());
    }

    @Test
    public void testUpdateFields_ThrowsSQLException() throws SQLException {
        String[] fields = {"field1"};
        Object[] values = {"value1"};
        String condition = "id = 1";

        Mockito.when(mockConnection.prepareStatement(Mockito.anyString())).thenThrow(new SQLException("SQL Error"));

        SQLException exception = Assertions.assertThrows(SQLException.class, () -> {
            genericDAO.updateFields(mockDbConnection, fields, values, condition);
        });

        Assertions.assertEquals("SQL Error", exception.getMessage());
    }
}