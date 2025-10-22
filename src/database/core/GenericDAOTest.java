import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.mockito.Mockito;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import database.core.GenericDAO;
import database.core.DBConnection;

public class GenericDAOTest {

    private GenericDAO<Object> genericDAO;
    private DBConnection mockDbConnection;
    private Connection mockConnection;
    private PreparedStatement mockPreparedStatement;

    @BeforeEach
    public void setUp() throws SQLException {
        genericDAO = new GenericDAO<Object>() {
            @Override
            protected String getTableName() {
                return "test_table";
            }
        };
        mockDbConnection = Mockito.mock(DBConnection.class);
        mockConnection = Mockito.mock(Connection.class);
        mockPreparedStatement = Mockito.mock(PreparedStatement.class);

        Mockito.when(mockDbConnection.getConnection()).thenReturn(mockConnection);
        Mockito.when(mockConnection.prepareStatement(Mockito.anyString())).thenReturn(mockPreparedStatement);
    }

    @AfterEach
    public void tearDown() {
        genericDAO = null;
        mockDbConnection = null;
        mockConnection = null;
        mockPreparedStatement = null;
    }

    @Test
    public void testUpdateFieldsSuccess() throws SQLException {
        String[] fields = {"field1", "field2"};
        Object[] values = {123, "value"};
        String condition = "id = 1";

        genericDAO.updateFields(mockDbConnection, fields, values, condition);

        Mockito.verify(mockConnection).prepareStatement("UPDATE test_table SET field1 = ?, field2 = ? WHERE id = 1");
        Mockito.verify(mockPreparedStatement).setObject(1, 123);
        Mockito.verify(mockPreparedStatement).setObject(2, "value");
        Mockito.verify(mockPreparedStatement).executeUpdate();
    }

    @Test
    public void testUpdateFieldsThrowsSQLException() throws SQLException {
        String[] fields = {"field1"};
        Object[] values = {123};
        String condition = "id = 1";

        Mockito.when(mockConnection.prepareStatement(Mockito.anyString())).thenThrow(new SQLException("Database error"));

        SQLException exception = Assertions.assertThrows(SQLException.class, () -> {
            genericDAO.updateFields(mockDbConnection, fields, values, condition);
        });

        Assertions.assertEquals("Database error", exception.getMessage());
    }

    @Test
    public void testUpdateFieldsThrowsIllegalArgumentException() {
        String[] fields = {"field1"};
        Object[] values = {123, "value"};
        String condition = "id = 1";

        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            genericDAO.updateFields(mockDbConnection, fields, values, condition);
        });

        Assertions.assertEquals("Le nombre de champs doit correspondre au nombre de valeurs.", exception.getMessage());
    }

    @Test
    public void testUpdateFieldsWithEmptyFieldsAndValues() throws SQLException {
        String[] fields = {};
        Object[] values = {};
        String condition = "1 = 1";

        genericDAO.updateFields(mockDbConnection, fields, values, condition);

        Mockito.verify(mockConnection).prepareStatement("UPDATE test_table SET  WHERE 1 = 1");
        Mockito.verify(mockPreparedStatement).executeUpdate();
    }
}