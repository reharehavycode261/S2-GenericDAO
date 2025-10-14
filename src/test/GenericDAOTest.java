package test;

import database.core.GenericDAO;
import database.core.DBConnection;
import database.provider.PostgreSQL;
import org.junit.jupiter.api.*;
import java.sql.Connection;

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

    @Test
    public void testCountAll() throws Exception {
        long count = genericDAO.count(null);
        assertEquals(expectedCountAllRecords, count, "Le nombre total d'enregistrements n'est pas correct");
    }
    
    @Test
    public void testCountWithCondition() throws Exception {
        long count = genericDAO.count("columnName = 'ExampleValue'");
        assertEquals(expectedCountWithCondition, count, "Le nombre d'enregistrements avec condition n'est pas correct");
    }
}