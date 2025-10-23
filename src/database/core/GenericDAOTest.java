package database.core;

import database.exception.SQL.AttributeMissingException;
import database.exception.object.NotIdentifiedInDatabaseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class GenericDAOTest {

    private GenericDAO<TestEntity> genericDAO;
    private DBConnection mockDbConnection;
    private Connection mockConnection;
    private PreparedStatement mockPreparedStatement;

    @BeforeEach
    void setUp() throws SQLException {
        genericDAO = new GenericDAO<>();
        mockDbConnection = mock(DBConnection.class);
        mockConnection = mock(Connection.class);
        mockPreparedStatement = mock(PreparedStatement.class);

        when(mockDbConnection.getConnection()).thenReturn(mockConnection);
        when(mockConnection.prepareStatement(any(String.class))).thenReturn(mockPreparedStatement);
    }

    @Test
    void testUpdateFieldsSuccess() throws Exception {
        TestEntity entity = new TestEntity("1", "oldValue");
        Map<String, Object> fieldsToUpdate = new HashMap<>();
        fieldsToUpdate.put("name", "newValue");

        genericDAO.updateFields(mockDbConnection, entity, fieldsToUpdate);

        verify(mockPreparedStatement, times(1)).setObject(1, "newValue");
        verify(mockPreparedStatement, times(1)).setObject(2, "1");
        verify(mockPreparedStatement, times(1)).executeUpdate();
    }

    @Test
    void testUpdateFieldsNoSuchFieldException() {
        TestEntity entity = new TestEntity("1", "oldValue");
        Map<String, Object> fieldsToUpdate = new HashMap<>();
        fieldsToUpdate.put("nonExistentField", "newValue");

        Assertions.assertThrows(NoSuchFieldException.class, () -> {
            genericDAO.updateFields(mockDbConnection, entity, fieldsToUpdate);
        });
    }

    @Test
    void testUpdateFieldsSQLException() throws Exception {
        TestEntity entity = new TestEntity("1", "oldValue");
        Map<String, Object> fieldsToUpdate = new HashMap<>();
        fieldsToUpdate.put("name", "newValue");

        when(mockConnection.prepareStatement(any(String.class))).thenThrow(new SQLException());

        Assertions.assertThrows(SQLException.class, () -> {
            genericDAO.updateFields(mockDbConnection, entity, fieldsToUpdate);
        });
    }

    @Test
    void testUpdateFieldsIllegalAccessException() throws Exception {
        TestEntity entity = new TestEntity("1", "oldValue");
        Map<String, Object> fieldsToUpdate = new HashMap<>();
        fieldsToUpdate.put("name", "newValue");

        Field field = TestEntity.class.getDeclaredField("name");
        field.setAccessible(false);

        Assertions.assertThrows(IllegalAccessException.class, () -> {
            genericDAO.updateFields(mockDbConnection, entity, fieldsToUpdate);
        });
    }

    @Test
    void testUpdateFieldsAttributeMissingException() {
        TestEntity entity = new TestEntity(null, "oldValue");
        Map<String, Object> fieldsToUpdate = new HashMap<>();
        fieldsToUpdate.put("name", "newValue");

        Assertions.assertThrows(AttributeMissingException.class, () -> {
            genericDAO.updateFields(mockDbConnection, entity, fieldsToUpdate);
        });
    }

    // Test entity class for testing purposes
    private static class TestEntity {
        private String id;
        private String name;

        public TestEntity(String id, String name) {
            this.id = id;
            this.name = name;
        }
    }
}