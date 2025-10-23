package database.core;

import org.junit.jupiter.api.*;
import java.sql.*;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class GenericDAOTest {

    private Connection connection;
    private GenericDAO<TestEntity> genericDAO;

    @BeforeEach
    public void setUp() throws SQLException {
        // Mise en place d'une connexion simulée à la base de données
        connection = DriverManager.getConnection("jdbc:h2:mem:testdb", "sa", "");
        genericDAO = new GenericDAO<>(connection, "TestEntity");

        // Initialize database with sample data for testing
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("CREATE TABLE TestEntity (id INT PRIMARY KEY, name VARCHAR(255))");
            for (int i = 1; i <= 50; i++) {
                stmt.execute("INSERT INTO TestEntity (id, name) VALUES (" + i + ", 'Name" + i + "')");
            }
        }
    }

    @Test
    public void testFindPage() throws SQLException {
        // Test de la première page
        List<TestEntity> firstPage = genericDAO.findPage(0, 10);
        assertEquals(10, firstPage.size(), "La taille de la première page doit être 10");
        assertEquals("Name1", firstPage.get(0).getName(), "Le premier élément de la première page doit être 'Name1'");

        // Test de la deuxième page
        List<TestEntity> secondPage = genericDAO.findPage(10, 10);
        assertEquals(10, secondPage.size(), "La taille de la deuxième page doit être 10");
        assertEquals("Name11", secondPage.get(0).getName(), "Le premier élément de la deuxième page doit être 'Name11'");
    }

    @Test
    public void testFindPageWithInvalidOffset() {
        // Test avec un offset invalide
        assertThrows(SQLException.class, () -> {
            genericDAO.findPage(-1, 10);
        }, "Une exception doit être lancée pour un offset négatif");
    }

    @Test
    public void testFindPageWithInvalidLimit() {
        // Test avec une limite invalide
        assertThrows(SQLException.class, () -> {
            genericDAO.findPage(0, -10);
        }, "Une exception doit être lancée pour une limite négative");
    }

    @Test
    public void testFindPageWithZeroLimit() throws SQLException {
        // Test avec une limite de zéro
        List<TestEntity> emptyPage = genericDAO.findPage(0, 0);
        assertEquals(0, emptyPage.size(), "La taille de la page doit être 0 pour une limite de 0");
    }

    @AfterEach
    public void tearDown() throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("DROP TABLE TestEntity");
        }
        connection.close();
    }

    // Classe fictive pour le test
    private static class TestEntity {
        private int id;
        private String name;

        public TestEntity(int id, String name) {
            this.id = id;
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
}