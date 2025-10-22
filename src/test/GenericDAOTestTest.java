package test;

import database.core.GenericDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.*;

public class GenericDAOTest {

    private Connection connection;
    private GenericDAO genericDAO;

    @BeforeEach
    public void setUp() throws SQLException {
        // Configure une connexion de test factice
        connection = DriverManager.getConnection("jdbc:h2:mem:testdb", "sa", "");
        genericDAO = new GenericDAO(connection);
    }

    @Test
    public void testUpdateFieldsSuccess() {
        try {
            String tableName = "emp";
            String whereClause = "id = 1";
            Map<String, Object> fieldsValues = new HashMap<>();
            fieldsValues.put("nom", "NouveauNom");
            fieldsValues.put("prenom", "NouveauPrenom");

            genericDAO.updateFields(tableName, fieldsValues, whereClause);

            // Asserts basés sur des vérifications sur la base de données
            // Exemple: vérifier que les valeurs ont été mises à jour correctement
            // Cela nécessiterait une requête pour récupérer les valeurs et les comparer
            // Assertions.assertEquals("NouveauNom", retrievedNom);
            // Assertions.assertEquals("NouveauPrenom", retrievedPrenom);

        } catch (SQLException e) {
            Assertions.fail("Exception non attendue: " + e.getMessage());
        }
    }

    @Test
    public void testUpdateFieldsFailure() {
        String tableName = "emp";
        Map<String, Object> fieldsValues = new HashMap<>();

        Assertions.assertThrows(SQLException.class, () -> {
            genericDAO.updateFields(tableName, fieldsValues, "id = unknown");
        });
    }

    @Test
    public void testUpdateFieldsWithEmptyFieldsValues() {
        String tableName = "emp";
        String whereClause = "id = 1";
        Map<String, Object> fieldsValues = new HashMap<>();

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            genericDAO.updateFields(tableName, fieldsValues, whereClause);
        });
    }

    @Test
    public void testUpdateFieldsWithNullTableName() {
        Map<String, Object> fieldsValues = new HashMap<>();
        fieldsValues.put("nom", "NouveauNom");

        Assertions.assertThrows(NullPointerException.class, () -> {
            genericDAO.updateFields(null, fieldsValues, "id = 1");
        });
    }

    @Test
    public void testUpdateFieldsWithNullWhereClause() {
        String tableName = "emp";
        Map<String, Object> fieldsValues = new HashMap<>();
        fieldsValues.put("nom", "NouveauNom");

        Assertions.assertThrows(NullPointerException.class, () -> {
            genericDAO.updateFields(tableName, fieldsValues, null);
        });
    }
}