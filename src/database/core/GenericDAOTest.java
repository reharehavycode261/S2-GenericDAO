package database.core;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.mockito.Mockito;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

class GenericDAOTest {

    private Connection mockConnection;
    private PreparedStatement mockPreparedStatement;
    private GenericDAO<Object> genericDAO;

    @BeforeEach
    void setUp() throws SQLException {
        // Mock the Connection and PreparedStatement
        mockConnection = Mockito.mock(Connection.class);
        mockPreparedStatement = Mockito.mock(PreparedStatement.class);

        // When the connection prepares a statement, return the mock PreparedStatement
        Mockito.when(mockConnection.prepareStatement(Mockito.anyString())).thenReturn(mockPreparedStatement);

        // Initialize the GenericDAO with the mock connection
        genericDAO = new GenericDAO<>(mockConnection, "test_table", "id");
    }

    @AfterEach
    void tearDown() {
        // Reset the mocks after each test
        Mockito.reset(mockConnection, mockPreparedStatement);
    }

    @Test
    void testUpdateFieldsSuccess() throws SQLException {
        // Arrange
        List<Affectation> updates = Arrays.asList(
            new Affectation("column1", "value1"),
            new Affectation("column2", "value2")
        );

        // Act
        genericDAO.updateFields(1, updates);

        // Assert
        Mockito.verify(mockPreparedStatement, Mockito.times(1)).setObject(1, "value1");
        Mockito.verify(mockPreparedStatement, Mockito.times(1)).setObject(2, "value2");
        Mockito.verify(mockPreparedStatement, Mockito.times(1)).setObject(3, 1);
        Mockito.verify(mockPreparedStatement, Mockito.times(1)).executeUpdate();
    }

    @Test
    void testUpdateFieldsWithNoUpdates() throws SQLException {
        // Arrange
        List<Affectation> updates = Arrays.asList();

        // Act & Assert
        Assertions.assertThrows(SQLException.class, () -> {
            genericDAO.updateFields(1, updates);
        });
    }

    @Test
    void testUpdateFieldsSQLException() throws SQLException {
        // Arrange
        List<Affectation> updates = Arrays.asList(
            new Affectation("column1", "value1")
        );

        // Simulate a SQL exception when executing the update
        Mockito.doThrow(new SQLException("SQL error")).when(mockPreparedStatement).executeUpdate();

        // Act & Assert
        Assertions.assertThrows(SQLException.class, () -> {
            genericDAO.updateFields(1, updates);
        });
    }

    @Test
    void testUpdateFieldsNullId() {
        // Arrange
        List<Affectation> updates = Arrays.asList(
            new Affectation("column1", "value1")
        );

        // Act & Assert
        Assertions.assertThrows(SQLException.class, () -> {
            genericDAO.updateFields(null, updates);
        });
    }
}

// Mock class for Affectation
class Affectation {
    private String column;
    private Object value;

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