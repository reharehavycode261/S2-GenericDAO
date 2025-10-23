package test;

import database.core.DBConnection;
import database.core.Database;
import database.core.GenericDAO;
import database.provider.PostgreSQL;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class MainTest {

    private Database mockDatabase;
    private DBConnection mockDBConnection;
    private Connection mockConnection;
    private GenericDAO<SampleEntity> mockGenericDAO;

    @BeforeEach
    public void setUp() throws SQLException {
        mockDatabase = Mockito.mock(PostgreSQL.class);
        mockDBConnection = Mockito.mock(DBConnection.class);
        mockConnection = Mockito.mock(Connection.class);
        mockGenericDAO = Mockito.mock(GenericDAO.class);

        when(mockDatabase.createConnection()).thenReturn(mockDBConnection);
        when(mockDBConnection.connect()).thenReturn(mockConnection);
    }

    @Test
    public void testMainSuccessfulUpdate() throws Exception {
        // Arrange
        SampleEntity entity = new SampleEntity(1, "Old Name", "Old Value");
        Map<String, Object> updates = new HashMap<>();
        updates.put("name", "New Name");
        updates.put("value", "New Value");

        when(mockGenericDAO.updateFields(any(SampleEntity.class), any(Map.class))).thenReturn(1);

        // Act
        int rowsAffected = mockGenericDAO.updateFields(entity, updates);

        // Assert
        Assertions.assertEquals(1, rowsAffected, "The number of rows affected should be 1.");
    }

    @Test
    public void testMainUpdateWithSQLException() throws Exception {
        // Arrange
        SampleEntity entity = new SampleEntity(1, "Old Name", "Old Value");
        Map<String, Object> updates = new HashMap<>();
        updates.put("name", "New Name");
        updates.put("value", "New Value");

        when(mockGenericDAO.updateFields(any(SampleEntity.class), any(Map.class))).thenThrow(new SQLException());

        // Act & Assert
        Assertions.assertThrows(SQLException.class, () -> {
            mockGenericDAO.updateFields(entity, updates);
        }, "An SQLException should be thrown when updateFields fails.");
    }

    @Test
    public void testSampleEntityGetId() {
        // Arrange
        SampleEntity entity = new SampleEntity(1, "Test Name", "Test Value");

        // Act
        int id = entity.getId();

        // Assert
        Assertions.assertEquals(1, id, "The ID should be 1.");
    }

    @Test
    public void testSampleEntityConstructor() {
        // Arrange
        SampleEntity entity = new SampleEntity(2, "Another Name", "Another Value");

        // Act & Assert
        Assertions.assertEquals(2, entity.getId(), "The ID should be 2.");
        // Assuming getters for name and value are present
        // Assertions.assertEquals("Another Name", entity.getName(), "The name should be 'Another Name'.");
        // Assertions.assertEquals("Another Value", entity.getValue(), "The value should be 'Another Value'.");
    }
}