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

public class GenericDAOTest {

    private Connection mockConnection;
    private PreparedStatement mockStatement;
    private GenericDAO<TestEntity> genericDAO;

    @BeforeEach
    public void setUp() throws SQLException {
        mockConnection = Mockito.mock(Connection.class);
        mockStatement = Mockito.mock(PreparedStatement.class);
        Mockito.when(mockConnection.prepareStatement(Mockito.anyString())).thenReturn(mockStatement);
        genericDAO = new GenericDAO<>("test_table", mockConnection);
    }

    @AfterEach
    public void tearDown() throws SQLException {
        Mockito.verifyNoMoreInteractions(mockConnection, mockStatement);
    }

    @Test
    public void testUpdateFields_SuccessfulUpdate() throws SQLException {
        // Arrange
        TestEntity entity = new TestEntity(1, "newValue");
        List<String> fieldsToUpdate = Arrays.asList("name");

        // Act
        genericDAO.updateFields(entity, fieldsToUpdate);

        // Assert
        Mockito.verify(mockConnection).prepareStatement("UPDATE test_table SET name = ? WHERE id = ?");
        Mockito.verify(mockStatement).setObject(1, "newValue");
        Mockito.verify(mockStatement).setObject(2, 1);
        Mockito.verify(mockStatement).executeUpdate();
    }

    @Test
    public void testUpdateFields_EmptyFieldsList_ThrowsException() {
        // Arrange
        TestEntity entity = new TestEntity(1, "value");

        // Act & Assert
        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            genericDAO.updateFields(entity, Arrays.asList());
        });
        Assertions.assertEquals("La liste des champs à mettre à jour ne peut pas être vide.", exception.getMessage());
    }

    @Test
    public void testUpdateFields_NullFieldsList_ThrowsException() {
        // Arrange
        TestEntity entity = new TestEntity(1, "value");

        // Act & Assert
        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            genericDAO.updateFields(entity, null);
        });
        Assertions.assertEquals("La liste des champs à mettre à jour ne peut pas être vide.", exception.getMessage());
    }

    @Test
    public void testUpdateFields_NoSuchFieldException_ThrowsSQLException() {
        // Arrange
        TestEntity entity = new TestEntity(1, "value");
        List<String> fieldsToUpdate = Arrays.asList("nonExistentField");

        // Act & Assert
        SQLException exception = Assertions.assertThrows(SQLException.class, () -> {
            genericDAO.updateFields(entity, fieldsToUpdate);
        });
        Assertions.assertTrue(exception.getMessage().contains("Erreur lors de la"));
    }

    @Test
    public void testUpdateFields_IllegalAccessException_ThrowsSQLException() throws NoSuchFieldException, IllegalAccessException {
        // Arrange
        TestEntity entity = new TestEntity(1, "value");
        List<String> fieldsToUpdate = Arrays.asList("name");

        // Simulate IllegalAccessException
        Field nameField = TestEntity.class.getDeclaredField("name");
        nameField.setAccessible(false);

        // Act & Assert
        SQLException exception = Assertions.assertThrows(SQLException.class, () -> {
            genericDAO.updateFields(entity, fieldsToUpdate);
        });
        Assertions.assertTrue(exception.getMessage().contains("Erreur lors de la"));
    }

    // Test entity class for testing purposes
    private static class TestEntity {
        private int id;
        private String name;

        public TestEntity(int id, String name) {
            this.id = id;
            this.name = name;
        }
    }
}