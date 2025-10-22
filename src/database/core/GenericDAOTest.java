package database.core;

import database.exception.SQL.AttributeMissingException;
import database.exception.SQL.AttributeTypeNotExistingException;
import database.exception.object.NotIdentifiedInDatabaseException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.mockito.Mockito;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

class GenericDAOTest {

    private GenericDAO genericDAO;
    private Connection mockConnection;
    private PreparedStatement mockPreparedStatement;

    @BeforeEach
    void setUp() throws SQLException {
        mockConnection = Mockito.mock(Connection.class);
        mockPreparedStatement = Mockito.mock(PreparedStatement.class);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);

        genericDAO = new GenericDAO();
        genericDAO.connection = mockConnection;
    }

    @AfterEach
    void tearDown() {
        genericDAO = null;
    }

    @Test
    void testUpdateFieldsSuccess() throws SQLException {
        // Arrange
        String tableName = "test_table";
        String id = "123";
        List<Affectation> updates = new ArrayList<>();
        updates.add(new Affectation("column1", "value1"));
        updates.add(new Affectation("column2", "value2"));

        // Act
        genericDAO.updateFields(tableName, id, updates);

        // Assert
        verify(mockPreparedStatement, times(1)).setObject(1, "value1");
        verify(mockPreparedStatement, times(1)).setObject(2, "value2");
        verify(mockPreparedStatement, times(1)).setString(3, id);
        verify(mockPreparedStatement, times(1)).executeUpdate();
    }

    @Test
    void testUpdateFieldsEmptyUpdates() {
        // Arrange
        String tableName = "test_table";
        String id = "123";
        List<Affectation> updates = new ArrayList<>();

        // Act & Assert
        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            genericDAO.updateFields(tableName, id, updates);
        });
        Assertions.assertEquals("No fields to update.", exception.getMessage());
    }

    @Test
    void testUpdateFieldsNullUpdates() {
        // Arrange
        String tableName = "test_table";
        String id = "123";

        // Act & Assert
        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            genericDAO.updateFields(tableName, id, null);
        });
        Assertions.assertEquals("No fields to update.", exception.getMessage());
    }

    @Test
    void testUpdateFieldsSQLException() throws SQLException {
        // Arrange
        String tableName = "test_table";
        String id = "123";
        List<Affectation> updates = new ArrayList<>();
        updates.add(new Affectation("column1", "value1"));

        doThrow(new SQLException("SQL error")).when(mockPreparedStatement).executeUpdate();

        // Act & Assert
        SQLException exception = Assertions.assertThrows(SQLException.class, () -> {
            genericDAO.updateFields(tableName, id, updates);
        });
        Assertions.assertEquals("Failed to update fields: SQL error", exception.getMessage());
    }
}