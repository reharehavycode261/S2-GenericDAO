package test;

import database.core.DBConnection;
import database.core.GenericDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

class GenericDAOTest {
    private GenericDAO<Object> dao;
    private DBConnection dbConnection;
    private Connection connection;

    @BeforeEach
    void setUp() throws SQLException {
        // Configurer une connexion en mémoire pour les tests
        connection = DriverManager.getConnection("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1");
        dbConnection = new DBConnection(null, connection);

        // Configuration initiale de la base de données
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("CREATE TABLE test_table (id INT PRIMARY KEY, name VARCHAR(255), age INT)");
            stmt.execute("INSERT INTO test_table (id, name, age) VALUES (1, 'Alice', 30)");
        }
        
        dao = new GenericDAO<>(connection);
    }

    @Test
    void testUpdateFields_Success() throws SQLException {
        // Mettre à jour le nom et l'âge de l'utilisateur avec id 1
        dao.updateFields(dbConnection, "test_table", new String[]{"name", "age"}, new Object[]{"Bob", 35}, "id = 1");

        // Vérification des modifications
        try (Statement stmt = connection.createStatement()) {
            var rs = stmt.executeQuery("SELECT name, age FROM test_table WHERE id = 1");
            if (rs.next()) {
                assertEquals("Bob", rs.getString("name"));
                assertEquals(35, rs.getInt("age"));
            } else {
                fail("Aucune donnée trouvée pour id = 1");
            }
        }
    }

    @Test
    void testUpdateFields_FieldValueMismatch() {
        // Vérifier si une exception est levée quand il y a incohérence entre nombre de champs et valeurs
        assertThrows(IllegalArgumentException.class, () -> {
            dao.updateFields(dbConnection, "test_table", new String[]{"name"}, new Object[]{"Bob", 35}, "id = 1");
        });
    }

    // Autres scénarios de test pourraient inclure des tests d'erreur SQL, transactions, etc.
}