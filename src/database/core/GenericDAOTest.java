import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.mockito.Mockito;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class GenericDAOTest {

    private Connection mockConnection;
    private PreparedStatement mockPreparedStatement;
    private GenericDAO<Object> genericDAO;

    @BeforeEach
    public void setUp() throws SQLException {
        mockConnection = Mockito.mock(Connection.class);
        mockPreparedStatement = Mockito.mock(PreparedStatement.class);
        Mockito.when(mockConnection.prepareStatement(Mockito.anyString())).thenReturn(mockPreparedStatement);
        genericDAO = new GenericDAO<>(mockConnection, "test_table");
    }

    @AfterEach
    public void tearDown() {
        Mockito.reset(mockConnection, mockPreparedStatement);
    }

    @Test
    public void testUpdateFieldsSuccess() throws SQLException {
        // Arrange
        Map<String, Object> fields = new HashMap<>();
        fields.put("name", "John Doe");
        fields.put("age", 30);

        // Act
        genericDAO.updateFields(1, fields);

        // Assert
        Mockito.verify(mockPreparedStatement, Mockito.times(1)).setObject(1, "John Doe");
        Mockito.verify(mockPreparedStatement, Mockito.times(1)).setObject(2, 30);
        Mockito.verify(mockPreparedStatement, Mockito.times(1)).setObject(3, 1);
        Mockito.verify(mockPreparedStatement, Mockito.times(1)).executeUpdate();
    }

    @Test
    public void testUpdateFieldsWithEmptyFields() {
        // Arrange
        Map<String, Object> fields = new HashMap<>();

        // Act & Assert
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            genericDAO.updateFields(1, fields);
        });
    }

    @Test
    public void testUpdateFieldsWithNullFields() {
        // Act & Assert
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            genericDAO.updateFields(1, null);
        });
    }

    @Test
    public void testUpdateFieldsSQLException() throws SQLException {
        // Arrange
        Map<String, Object> fields = new HashMap<>();
        fields.put("name", "John Doe");
        Mockito.when(mockConnection.prepareStatement(Mockito.anyString())).thenThrow(new SQLException());

        // Act & Assert
        Assertions.assertThrows(SQLException.class, () -> {
            genericDAO.updateFields(1, fields);
        });
    }
}