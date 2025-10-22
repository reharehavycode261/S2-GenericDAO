package test.database.core;

import database.core.DBConnection;
import org.junit.jupiter.api.*;
import org.mockito.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.mockito.Mockito.*;

public class DBConnectionTest {

    @Mock
    Connection mockConnection;

    @Mock
    PreparedStatement mockStatement;

    DBConnection dbConnection;

    @BeforeEach
    public void setup() throws SQLException {
        MockitoAnnotations.openMocks(this);
        dbConnection = new DBConnection(null, mockConnection);

        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
    }

    /**
     * Teste la mise à jour réussie des champs dans la base de données.
     */
    @Test
    public void testUpdateFieldsSuccess() throws SQLException {
        String[] fields = {"name", "age"};
        Object[] values = {"John Doe", 30};
        String condition = "id = 1";

        dbConnection.updateFields("users", fields, values, condition);

        verify(mockStatement).setObject(1, "John Doe");
        verify(mockStatement).setObject(2, 30);
        verify(mockStatement).executeUpdate();
    }

    /**
     * Teste le scénario où le nombre de champs ne correspond pas au nombre de valeurs.
     * Doit lancer une IllegalArgumentException.
     */
    @Test
    public void testUpdateFieldsIllegalArgumentException() {
        String[] fields = {"name", "age"};
        Object[] values = {"John Doe"};
        String condition = "id = 1";

        Assertions.assertThrows(IllegalArgumentException.class, () ->
                dbConnection.updateFields("users", fields, values, condition));
    }

    /**
     * Teste le scénario où une SQLException est lancée lors de l'exécution de la mise à jour.
     */
    @Test
    public void testUpdateFieldsSQLExecution() throws SQLException {
        String[] fields = {"name", "age"};
        Object[] values = {"John Doe", 30};
        String condition = "id = 1";

        doThrow(new SQLException()).when(mockStatement).executeUpdate();

        Assertions.assertThrows(SQLException.class, () ->
                dbConnection.updateFields("users", fields, values, condition));
    }

    /**
     * Teste le scénario où la condition est vide ou nulle.
     * Doit lancer une IllegalArgumentException.
     */
    @Test
    public void testUpdateFieldsEmptyCondition() {
        String[] fields = {"name", "age"};
        Object[] values = {"John Doe", 30};

        Assertions.assertThrows(IllegalArgumentException.class, () ->
                dbConnection.updateFields("users", fields, values, null));

        Assertions.assertThrows(IllegalArgumentException.class, () ->
                dbConnection.updateFields("users", fields, values, ""));
    }

    /**
     * Teste le scénario où le tableau de champs est vide.
     * Doit lancer une IllegalArgumentException.
     */
    @Test
    public void testUpdateFieldsEmptyFields() {
        String[] fields = {};
        Object[] values = {};
        String condition = "id = 1";

        Assertions.assertThrows(IllegalArgumentException.class, () ->
                dbConnection.updateFields("users", fields, values, condition));
    }

    /**
     * Teste le scénario où le tableau de valeurs est vide.
     * Doit lancer une IllegalArgumentException.
     */
    @Test
    public void testUpdateFieldsEmptyValues() {
        String[] fields = {"name", "age"};
        Object[] values = {};
        String condition = "id = 1";

        Assertions.assertThrows(IllegalArgumentException.class, () ->
                dbConnection.updateFields("users", fields, values, condition));
    }
}