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
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.*;

class GenericDAOTest {

    private GenericDAO genericDAO;
    private DBConnection dbConnectionMock;
    private Connection connectionMock;
    private PreparedStatement preparedStatementMock;

    @BeforeEach
    void setUp() throws SQLException {
        genericDAO = new GenericDAO();
        dbConnectionMock = mock(DBConnection.class);
        connectionMock = mock(Connection.class);
        preparedStatementMock = mock(PreparedStatement.class);

        when(dbConnectionMock.getConnection()).thenReturn(connectionMock);
        when(connectionMock.prepareStatement(anyString())).thenReturn(preparedStatementMock);
    }

    @AfterEach
    void tearDown() throws SQLException {
        connectionMock.close();
        preparedStatementMock.close();
    }

    /**
     * Teste la mise à jour réussie des champs avec des valeurs valides.
     */
    @Test
    void testUpdateFieldsSuccess() throws SQLException {
        Map<String, Object> fieldsToUpdate = new HashMap<>();
        fieldsToUpdate.put("name", "John Doe");
        fieldsToUpdate.put("age", 30);

        genericDAO.updateFields(dbConnectionMock, "users", "123", fieldsToUpdate);

        verify(preparedStatementMock, times(1)).setObject(1, "John Doe");
        verify(preparedStatementMock, times(1)).setObject(2, 30);
        verify(preparedStatementMock, times(1)).setObject(3, "123");
        verify(preparedStatementMock, times(1)).executeUpdate();
    }

    /**
     * Teste le cas où fieldsToUpdate est null.
     */
    @Test
    void testUpdateFieldsWithNullFieldsToUpdate() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            genericDAO.updateFields(dbConnectionMock, "users", "123", null);
        });
    }

    /**
     * Teste le cas où fieldsToUpdate est vide.
     */
    @Test
    void testUpdateFieldsWithEmptyFieldsToUpdate() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            genericDAO.updateFields(dbConnectionMock, "users", "123", new HashMap<>());
        });
    }

    /**
     * Teste le cas où une SQLException est levée lors de la préparation de la requête.
     */
    @Test
    void testUpdateFieldsSQLException() throws SQLException {
        when(connectionMock.prepareStatement(anyString())).thenThrow(new SQLException("Erreur SQL"));

        Map<String, Object> fieldsToUpdate = new HashMap<>();
        fieldsToUpdate.put("name", "John Doe");

        Assertions.assertThrows(SQLException.class, () -> {
            genericDAO.updateFields(dbConnectionMock, "users", "123", fieldsToUpdate);
        });
    }
}