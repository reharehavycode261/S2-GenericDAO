package test;

import database.core.DBConnection;
import database.core.GenericDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.*;

public class GenericDAOTest {

    private GenericDAO genericDAO;
    private DBConnection dbConnection;

    @BeforeEach
    public void setUp() {
        genericDAO = new GenericDAO();
        dbConnection = mock(DBConnection.class);
    }

    /**
     * Teste la méthode updateFields pour vérifier qu'elle appelle correctement
     * la méthode getConnection de DBConnection avec les bons paramètres.
     */
    @Test
    public void testUpdateFields() throws SQLException {
        Map<String, Object> fieldsToUpdate = new HashMap<>();
        fieldsToUpdate.put("name", "Updated Name");
        fieldsToUpdate.put("age", 30);

        genericDAO.updateFields(dbConnection, "example_table", "1", fieldsToUpdate);

        // Vérification que la méthode getConnection est appelée une fois
        verify(dbConnection, times(1)).getConnection();
    }

    /**
     * Teste la méthode updateFields pour vérifier qu'une exception est lancée
     * lorsque la connexion à la base de données échoue.
     */
    @Test
    public void testUpdateFieldsThrowsSQLException() throws SQLException {
        Map<String, Object> fieldsToUpdate = new HashMap<>();
        fieldsToUpdate.put("name", "Updated Name");
        fieldsToUpdate.put("age", 30);

        // Simule une SQLException lors de l'appel à getConnection
        when(dbConnection.getConnection()).thenThrow(new SQLException("Connection failed"));

        SQLException exception = Assertions.assertThrows(SQLException.class, () -> {
            genericDAO.updateFields(dbConnection, "example_table", "1", fieldsToUpdate);
        });

        // Vérification du message d'exception
        Assertions.assertEquals("Connection failed", exception.getMessage(), "L'exception doit avoir le message correct");
    }

    /**
     * Teste la méthode updateFields pour vérifier qu'une exception est lancée
     * lorsque le nom de la table est null.
     */
    @Test
    public void testUpdateFieldsWithNullTableName() {
        Map<String, Object> fieldsToUpdate = new HashMap<>();
        fieldsToUpdate.put("name", "Updated Name");
        fieldsToUpdate.put("age", 30);

        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            genericDAO.updateFields(dbConnection, null, "1", fieldsToUpdate);
        });

        // Vérification du message d'exception
        Assertions.assertEquals("Table name cannot be null", exception.getMessage(), "L'exception doit avoir le message correct");
    }

    /**
     * Teste la méthode updateFields pour vérifier qu'une exception est lancée
     * lorsque le map des champs à mettre à jour est vide.
     */
    @Test
    public void testUpdateFieldsWithEmptyFieldsMap() {
        Map<String, Object> fieldsToUpdate = new HashMap<>();

        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            genericDAO.updateFields(dbConnection, "example_table", "1", fieldsToUpdate);
        });

        // Vérification du message d'exception
        Assertions.assertEquals("Fields to update cannot be empty", exception.getMessage(), "L'exception doit avoir le message correct");
    }
}