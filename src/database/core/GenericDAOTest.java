package database.core;

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

public class GenericDAOTest {

    private GenericDAO<Object> genericDAO;
    private Connection mockConnection;
    private PreparedStatement mockPreparedStatement;

    @BeforeEach
    public void setUp() throws SQLException {
        mockConnection = Mockito.mock(Connection.class);
        mockPreparedStatement = Mockito.mock(PreparedStatement.class);
        Mockito.when(mockConnection.prepareStatement(Mockito.anyString())).thenReturn(mockPreparedStatement);

        genericDAO = new GenericDAO<>();
        // Utilisation de la réflexion pour injecter des dépendances privées
        setPrivateField(genericDAO, "connection", mockConnection);
        setPrivateField(genericDAO, "tableName", "test_table");
    }

    @AfterEach
    public void tearDown() {
        genericDAO = null;
        mockConnection = null;
        mockPreparedStatement = null;
    }

    @Test
    public void testUpdateFields_SuccessfulUpdate() throws SQLException {
        List<Affectation> updates = new ArrayList<>();
        updates.add(new Affectation("column1", "value1"));
        updates.add(new Affectation("column2", 123));

        genericDAO.updateFields(1, updates);

        Mockito.verify(mockPreparedStatement, Mockito.times(1)).setObject(1, "value1");
        Mockito.verify(mockPreparedStatement, Mockito.times(1)).setObject(2, 123);
        Mockito.verify(mockPreparedStatement, Mockito.times(1)).setObject(3, 1);
        Mockito.verify(mockPreparedStatement, Mockito.times(1)).executeUpdate();
    }

    @Test
    public void testUpdateFields_EmptyUpdates_ThrowsException() {
        List<Affectation> updates = new ArrayList<>();

        Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            genericDAO.updateFields(1, updates);
        });

        Assertions.assertEquals("Les mises à jour ne peuvent pas être nulles ou vides.", exception.getMessage());
    }

    @Test
    public void testUpdateFields_NullUpdates_ThrowsException() {
        Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            genericDAO.updateFields(1, null);
        });

        Assertions.assertEquals("Les mises à jour ne peuvent pas être nulles ou vides.", exception.getMessage());
    }

    @Test
    public void testUpdateFields_SQLExceptionThrown() throws SQLException {
        List<Affectation> updates = new ArrayList<>();
        updates.add(new Affectation("column1", "value1"));

        Mockito.when(mockConnection.prepareStatement(Mockito.anyString())).thenThrow(new SQLException("SQL error"));

        SQLException exception = Assertions.assertThrows(SQLException.class, () -> {
            genericDAO.updateFields(1, updates);
        });

        Assertions.assertEquals("SQL error", exception.getMessage());
    }

    /**
     * Utilitaire pour définir un champ privé via réflexion.
     */
    private void setPrivateField(Object target, String fieldName, Object value) {
        try {
            java.lang.reflect.Field field = target.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}