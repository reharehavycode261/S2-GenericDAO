package test;

import database.core.GenericDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mockito;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.*;

public class GenericDAOTest {
    private GenericDAO dao;
    private Connection connection;

    @BeforeEach
    public void setUp() throws SQLException {
        // Initialisation de la connexion à une base de données en mémoire pour les tests
        connection = DriverManager.getConnection("jdbc:h2:mem:testdb", "sa", "");
        dao = new YourConcreteDAO(connection); // Remplacer par la classe concrète de DAO
    }

    @AfterEach
    public void tearDown() throws SQLException {
        // Fermeture de la connexion après chaque test
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

    @Test
    public void testUpdateFields_SuccessfulUpdate() throws SQLException {
        // Créer un enregistrement pour le test
        Object id = createTestRecord();

        // Définir les nouvelles valeurs de champ
        Map<String, Object> fields = new HashMap<>();
        fields.put("nom", "Nouveau Nom");
        fields.put("prenom", "Nouveau Prenom");

        // Appeler la méthode à tester
        dao.updateFields(id, fields);

        // Vérifier les résultats
        Assertions.assertEquals("Nouveau Nom", getFieldValue(id, "nom"), "Le champ 'nom' n'a pas été mis à jour correctement.");
        Assertions.assertEquals("Nouveau Prenom", getFieldValue(id, "prenom"), "Le champ 'prenom' n'a pas été mis à jour correctement.");
    }

    @Test
    public void testUpdateFields_NullId() {
        // Définir les nouvelles valeurs de champ
        Map<String, Object> fields = new HashMap<>();
        fields.put("nom", "Nouveau Nom");

        // Vérifier que l'appel avec un id null lance une exception
        Executable executable = () -> dao.updateFields(null, fields);
        Assertions.assertThrows(IllegalArgumentException.class, executable, "L'appel avec un id null aurait dû lancer une IllegalArgumentException.");
    }

    @Test
    public void testUpdateFields_EmptyFields() throws SQLException {
        // Créer un enregistrement pour le test
        Object id = createTestRecord();

        // Appeler la méthode à tester avec un map vide
        dao.updateFields(id, new HashMap<>());

        // Vérifier que les valeurs restent inchangées
        Assertions.assertEquals("Nom Initial", getFieldValue(id, "nom"), "Le champ 'nom' aurait dû rester inchangé.");
        Assertions.assertEquals("Prenom Initial", getFieldValue(id, "prenom"), "Le champ 'prenom' aurait dû rester inchangé.");
    }

    @Test
    public void testUpdateFields_SQLException() throws SQLException {
        // Mock de la connexion pour simuler une SQLException
        Connection mockConnection = Mockito.mock(Connection.class);
        when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("Erreur de base de données"));
        dao = new YourConcreteDAO(mockConnection);

        // Créer un enregistrement pour le test
        Object id = createTestRecord();

        // Définir les nouvelles valeurs de champ
        Map<String, Object> fields = new HashMap<>();
        fields.put("nom", "Nouveau Nom");

        // Vérifier que l'appel lance une SQLException
        Executable executable = () -> dao.updateFields(id, fields);
        Assertions.assertThrows(SQLException.class, executable, "Une SQLException aurait dû être lancée.");
    }

    // Méthodes auxiliaires pour créer un enregistrement de test et récupérer les valeurs
    private Object createTestRecord() {
        // Implémentation fictive pour créer un enregistrement de test
        // Retourner un identifiant d'enregistrement fictif
        return 1;
    }

    private String getFieldValue(Object id, String fieldName) {
        // Implémentation fictive pour récupérer la valeur d'un champ
        // Retourner une valeur fictive basée sur le nom du champ
        if ("nom".equals(fieldName)) {
            return "Nom Initial";
        } else if ("prenom".equals(fieldName)) {
            return "Prenom Initial";
        }
        return null;
    }
}