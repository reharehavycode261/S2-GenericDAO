package test;

import database.core.GenericDAO;
import database.core.DBConnection;
import database.core.Database;
import org.junit.jupiter.api.*;
import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;

public class GenericDAOTest {

    private static DBConnection dbConnection;
    private static Connection connection;
    private static GenericDAO<Object> genericDAO;

    @BeforeAll
    public static void setup() throws SQLException {
        // Assurez-vous que la connexion à la base de données pointe vers une base de données de test
        dbConnection = new DBConnection(new Database("jdbc:your_database_url", "username", "password"));
        connection = dbConnection.getConnection();

        // Initialiser la table avec quelques données de test
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("CREATE TABLE IF NOT EXISTS test_table (id INT PRIMARY KEY, name VARCHAR(100))");
            stmt.execute("INSERT INTO test_table(id, name) VALUES (1, 'Test1'), (2, 'Test2')");
        }

        genericDAO = new GenericDAO<>(connection, "test_table");
    }

    @Test
    public void testCount() throws SQLException {
        // Test positif: Vérifier que le nombre d'enregistrements est correct
        long count = genericDAO.count();
        assertEquals(2, count, "Le nombre d'enregistrements devrait être de 2");
    }

    @Test
    public void testCountEmptyTable() throws SQLException {
        // Test négatif: Vérifier le comportement avec une table vide
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("DELETE FROM test_table");
        }
        long count = genericDAO.count();
        assertEquals(0, count, "Le nombre d'enregistrements devrait être de 0 après suppression");
    }

    @Test
    public void testCountWithException() {
        // Test négatif: Simuler une exception SQL
        assertThrows(SQLException.class, () -> {
            Connection invalidConnection = DriverManager.getConnection("jdbc:invalid_url", "username", "password");
            GenericDAO<Object> invalidDAO = new GenericDAO<>(invalidConnection, "test_table");
            invalidDAO.count();
        }, "Une SQLException devrait être lancée pour une connexion invalide");
    }

    @AfterAll
    public static void teardown() throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("DROP TABLE IF EXISTS test_table");
        }
        connection.close();
    }
}