package test;

import database.core.GenericDAO;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

public class GenericDAOTest {

    private Connection connection;
    private GenericDAO genericDAO;

    @Before
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

        } catch (SQLException e) {
            fail("Exception non attendue: " + e.getMessage());
        }
    }

    @Test(expected = SQLException.class)
    public void testUpdateFieldsFailure() throws SQLException {
        String tableName = "emp";
        Map<String, Object> fieldsValues = new HashMap<>();

        genericDAO.updateFields(tableName, fieldsValues, "id = unknown");
    }
}