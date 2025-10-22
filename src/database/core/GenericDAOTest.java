import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.mockito.Mockito;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GenericDAOTest {

    private GenericDAO<Object> genericDAO;
    private Connection mockConnection;
    private PreparedStatement mockPreparedStatement;

    @BeforeEach
    public void setUp() throws SQLException {
        // Setup mock objects
        mockConnection = Mockito.mock(Connection.class);
        mockPreparedStatement = Mockito.mock(PreparedStatement.class);

        // Setup the GenericDAO with mock connection and table name
        genericDAO = new GenericDAO<>();
        setPrivateField(genericDAO, "connection", mockConnection);
        setPrivateField(genericDAO, "tableName", "test_table");

        // Mock the behavior of the connection
        Mockito.when(mockConnection.prepareStatement(Mockito.anyString())).thenReturn(mockPreparedStatement);
    }

    @AfterEach
    public void tearDown() {
        // Clean up resources if needed
    }

    @Test
    public void testUpdateFields_Success() throws SQLException {
        // Arrange
        List<Affectation> updates = Arrays.asList(
                new Affectation("column1", "value1"),
                new Affectation("column2", "value2")
        );
        String whereClause = "id = 1";

        // Act
        genericDAO.updateFields(updates, whereClause);

        // Assert
        Mockito.verify(mockConnection).prepareStatement("UPDATE test_table SET column1 = ?, column2 = ? WHERE id = 1");
        Mockito.verify(mockPreparedStatement).setObject(1, "value1");
        Mockito.verify(mockPreparedStatement).setObject(2, "value2");
        Mockito.verify(mockPreparedStatement).executeUpdate();
    }

    @Test
    public void testUpdateFields_EmptyUpdates_ThrowsException() {
        // Arrange
        List<Affectation> updates = Collections.emptyList();
        String whereClause = "id = 1";

        // Act & Assert
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            genericDAO.updateFields(updates, whereClause);
        }, "La liste des mises à jour ne peut être vide.");
    }

    @Test
    public void testUpdateFields_NullUpdates_ThrowsException() {
        // Arrange
        List<Affectation> updates = null;
        String whereClause = "id = 1";

        // Act & Assert
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            genericDAO.updateFields(updates, whereClause);
        }, "La liste des mises à jour ne peut être vide.");
    }

    @Test
    public void testUpdateFields_SQLException() throws SQLException {
        // Arrange
        List<Affectation> updates = Arrays.asList(
                new Affectation("column1", "value1")
        );
        String whereClause = "id = 1";

        // Mock SQLException
        Mockito.when(mockConnection.prepareStatement(Mockito.anyString())).thenThrow(new SQLException("SQL error"));

        // Act & Assert
        Assertions.assertThrows(SQLException.class, () -> {
            genericDAO.updateFields(updates, whereClause);
        }, "SQL error");
    }

    /**
     * Util function to set private fields using reflection
     */
    private void setPrivateField(Object target, String fieldName, Object value) {
        try {
            java.lang.reflect.Field field = target.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Mock class for Affectation
     */
    private static class Affectation {
        private final String column;
        private final Object value;

        public Affectation(String column, Object value) {
            this.column = column;
            this.value = value;
        }

        public String getColumn() {
            return column;
        }

        public Object getValue() {
            return value;
        }
    }
}