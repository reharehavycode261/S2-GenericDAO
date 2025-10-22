package database.core;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.mockito.Mockito;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.mockito.Mockito.*;

public class GenericDAOTest {

    private GenericDAO<Object> genericDAO;
    private Connection mockConnection;
    private PreparedStatement mockPreparedStatement;

    @BeforeEach
    public void setUp() throws SQLException {
        genericDAO = new GenericDAO<>();
        mockConnection = Mockito.mock(Connection.class);
        mockPreparedStatement = Mockito.mock(PreparedStatement.class);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
    }

    @AfterEach
    public void tearDown() {
        genericDAO = null;
        mockConnection = null;
        mockPreparedStatement = null;
    }

    /**
     * Test mise à jour réussie des champs avec des valeurs valides.
     */
    @Test
    public void testUpdateFieldsSuccess() throws SQLException {
        String[] fields = {"name", "age"};
        Object[] values = {"John Doe", 30};
        String condition = "id = 1";

        genericDAO.updateFields(mockConnection, fields, values, condition);

        verify(mockConnection, times(1)).prepareStatement(anyString());
        verify(mockPreparedStatement, times(1)).setObject(1, "John Doe");
        verify(mockPreparedStatement, times(1)).setObject(2, 30);
        verify(mockPreparedStatement, times(1)).executeUpdate();
    }

    /**
     * Test mise à jour échoue lorsque le nombre de champs ne correspond pas au nombre de valeurs.
     */
    @Test
    public void testUpdateFieldsMismatchFieldsAndValues() {
        String[] fields = {"name", "age"};
        Object[] values = {"John Doe"};

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            genericDAO.updateFields(mockConnection, fields, values, "id = 1");
        });
    }

    /**
     * Test mise à jour échoue en cas d'erreur SQL.
     */
    @Test
    public void testUpdateFieldsSQLException() throws SQLException {
        String[] fields = {"name"};
        Object[] values = {"John Doe"};
        String condition = "id = 1";

        when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("Erreur SQL"));

        Assertions.assertThrows(SQLException.class, () -> {
            genericDAO.updateFields(mockConnection, fields, values, condition);
        });
    }

    /**
     * Test mise à jour échoue avec une condition vide.
     */
    @Test
    public void testUpdateFieldsEmptyCondition() {
        String[] fields = {"name"};
        Object[] values = {"John Doe"};
        String condition = "";

        Assertions.assertThrows(SQLException.class, () -> {
            genericDAO.updateFields(mockConnection, fields, values, condition);
        });
    }

    /**
     * Test mise à jour échoue avec des champs vides.
     */
    @Test
    public void testUpdateFieldsEmptyFields() {
        String[] fields = {};
        Object[] values = {};
        String condition = "id = 1";

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            genericDAO.updateFields(mockConnection, fields, values, condition);
        });
    }
}