package test;

import database.core.GenericDAO;
import database.core.DBConnection;
import database.provider.PostgreSQL;
import org.junit.jupiter.api.*;
import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

public class GenericDAOTest {
    private static Connection connection;
    private static GenericDAO<Object> genericDAO;

    @BeforeAll
    public static void setUp() throws Exception {
        // Configurer la connexion à la base de données pour les tests
        connection = new PostgreSQL("localhost", "5432", "dao", "", "").createConnection();
        genericDAO = new GenericDAO<>(connection, "TestTable");
        // Initialise la table pour les tests (à mettre en place avant les tests)
    }

    @AfterAll
    public static void tearDown() throws Exception {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

    @Test
    public void testCountAll() throws Exception {
        // Assurez-vous que la table est initialisée avec un nombre connu d'enregistrements
        long count = genericDAO.count(null);
        long expectedCountAllRecords = 10; // Remplacer par le nombre attendu d'enregistrements
        assertEquals(expectedCountAllRecords, count, "Le nombre total d'enregistrements n'est pas correct");
    }

    @Test
    public void testCountWithCondition() throws Exception {
        // Assurez-vous que la table est initialisée avec un nombre connu d'enregistrements correspondant à la condition
        long count = genericDAO.count("columnName = 'ExampleValue'");
        long expectedCountWithCondition = 5; // Remplacer par le nombre attendu d'enregistrements
        assertEquals(expectedCountWithCondition, count, "Le nombre d'enregistrements avec condition n'est pas correct");
    }

    @Test
    public void testCountWithInvalidCondition() {
        // Teste le comportement avec une condition invalide
        Exception exception = assertThrows(SQLException.class, () -> {
            genericDAO.count("invalid SQL syntax");
        });
        assertTrue(exception.getMessage().contains("syntax"), "L'exception attendue n'a pas été levée pour une condition invalide");
    }

    @Test
    public void testCountWithNullConnection() {
        // Teste le comportement lorsque la connexion est nulle
        GenericDAO<Object> daoWithNullConnection = new GenericDAO<>(null, "TestTable");
        Exception exception = assertThrows(NullPointerException.class, () -> {
            daoWithNullConnection.count(null);
        });
        assertNotNull(exception, "Une NullPointerException était attendue lorsque la connexion est nulle");
    }
}