import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.mockito.Mockito;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import database.core.GenericDAO;

class GenericDAOTest {

    private Connection mockConnection;
    private PreparedStatement mockPreparedStatement;
    private GenericDAO<Object> genericDAO;

    @BeforeEach
    void setUp() throws SQLException {
        mockConnection = Mockito.mock(Connection.class);
        mockPreparedStatement = Mockito.mock(PreparedStatement.class);
        Mockito.when(mockConnection.prepareStatement(Mockito.anyString())).thenReturn(mockPreparedStatement);
        genericDAO = new GenericDAO<>(mockConnection);
    }

    @AfterEach
    void tearDown() {
        Mockito.reset(mockConnection, mockPreparedStatement);
    }

    @Test
    void testUpdateFieldsSuccess() throws SQLException {
        // Arrange
        String[] fields = {"name", "age"};
        Object[] values = {"John Doe", 30};
        String condition = "id = 1";
        DBConnection dbConnection = Mockito.mock(DBConnection.class);
        Mockito.when(dbConnection.getConnection()).thenReturn(mockConnection);

        // Act
        genericDAO.updateFields(dbConnection, "users", fields, values, condition);

        // Assert
        Mockito.verify(mockConnection).prepareStatement("UPDATE users SET name = ?, age = ? WHERE id = 1");
        Mockito.verify(mockPreparedStatement).setObject(1, "John Doe");
        Mockito.verify(mockPreparedStatement).setObject(2, 30);
        Mockito.verify(mockPreparedStatement).executeUpdate();
    }

    @Test
    void testUpdateFieldsThrowsSQLException() throws SQLException {
        // Arrange
        String[] fields = {"name"};
        Object[] values = {"John Doe"};
        String condition = "id = 1";
        DBConnection dbConnection = Mockito.mock(DBConnection.class);
        Mockito.when(dbConnection.getConnection()).thenReturn(mockConnection);
        Mockito.when(mockConnection.prepareStatement(Mockito.anyString())).thenThrow(new SQLException("Database error"));

        // Act & Assert
        Assertions.assertThrows(SQLException.class, () -> {
            genericDAO.updateFields(dbConnection, "users", fields, values, condition);
        });
    }

    @Test
    void testUpdateFieldsThrowsIllegalArgumentException() {
        // Arrange
        String[] fields = {"name"};
        Object[] values = {"John Doe", 30};
        String condition = "id = 1";
        DBConnection dbConnection = Mockito.mock(DBConnection.class);

        // Act & Assert
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            genericDAO.updateFields(dbConnection, "users", fields, values, condition);
        });
    }

    @Test
    void testUpdateFieldsWithEmptyFieldsAndValues() throws SQLException {
        // Arrange
        String[] fields = {};
        Object[] values = {};
        String condition = "id = 1";
        DBConnection dbConnection = Mockito.mock(DBConnection.class);
        Mockito.when(dbConnection.getConnection()).thenReturn(mockConnection);

        // Act & Assert
        Assertions.assertThrows(SQLException.class, () -> {
            genericDAO.updateFields(dbConnection, "users", fields, values, condition);
        });
    }
}