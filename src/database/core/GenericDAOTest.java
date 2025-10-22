package database.core;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.mockito.Mockito;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

class GenericDAOTest {

    private Connection mockConnection;
    private PreparedStatement mockPreparedStatement;
    private GenericDAO<TestEntity> genericDAO;

    @BeforeEach
    void setUp() throws SQLException {
        MockitoAnnotations.openMocks(this);
        mockConnection = Mockito.mock(Connection.class);
        mockPreparedStatement = Mockito.mock(PreparedStatement.class);
        Mockito.when(mockConnection.prepareStatement(Mockito.anyString())).thenReturn(mockPreparedStatement);
        genericDAO = new GenericDAO<>(mockConnection, "test_table");
    }

    @AfterEach
    void tearDown() {
        Mockito.reset(mockConnection, mockPreparedStatement);
    }

    @Test
    void testUpdateFieldsSuccess() throws SQLException, IllegalAccessException {
        // Arrange
        TestEntity entity = new TestEntity();
        entity.setField1("value1");
        entity.setField2(123);

        Mockito.when(mockPreparedStatement.executeUpdate()).thenReturn(1);

        // Act
        boolean result = genericDAO.updateFields(entity, "id = 1");

        // Assert
        Assertions.assertTrue(result, "The update should be successful.");
        Mockito.verify(mockPreparedStatement, Mockito.times(1)).executeUpdate();
        Mockito.verify(mockPreparedStatement, Mockito.times(1)).setObject(1, "value1");
        Mockito.verify(mockPreparedStatement, Mockito.times(1)).setObject(2, 123);
    }

    @Test
    void testUpdateFieldsNoFieldsToUpdate() throws SQLException, IllegalAccessException {
        // Arrange
        TestEntity entity = new TestEntity();

        // Act
        boolean result = genericDAO.updateFields(entity, "id = 1");

        // Assert
        Assertions.assertFalse(result, "The update should not be successful as there are no fields to update.");
        Mockito.verify(mockPreparedStatement, Mockito.never()).executeUpdate();
    }

    @Test
    void testUpdateFieldsSQLException() throws SQLException, IllegalAccessException {
        // Arrange
        TestEntity entity = new TestEntity();
        entity.setField1("value1");

        Mockito.when(mockPreparedStatement.executeUpdate()).thenThrow(new SQLException("SQL error"));

        // Act & Assert
        SQLException exception = Assertions.assertThrows(SQLException.class, () -> {
            genericDAO.updateFields(entity, "id = 1");
        });

        Assertions.assertEquals("SQL error", exception.getMessage(), "The exception message should match.");
    }

    @Test
    void testConstructor() {
        // Assert
        Assertions.assertNotNull(genericDAO, "The GenericDAO instance should be created.");
        Assertions.assertEquals("test_table", genericDAO.tableName, "The table name should be set correctly.");
        Assertions.assertEquals(mockConnection, genericDAO.connection, "The connection should be set correctly.");
    }

    // Test entity class for testing purposes
    static class TestEntity {
        private String field1;
        private Integer field2;

        public String getField1() {
            return field1;
        }

        public void setField1(String field1) {
            this.field1 = field1;
        }

        public Integer getField2() {
            return field2;
        }

        public void setField2(Integer field2) {
            this.field2 = field2;
        }
    }
}