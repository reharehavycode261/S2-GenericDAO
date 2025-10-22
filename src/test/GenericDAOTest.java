package test;

import database.core.GenericDAO;
import java.sql.*;
import java.util.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class GenericDAOTest {
    private static Connection connection;
    private static GenericDAO<Emp> empDAO;

    @BeforeAll
    public static void setup() throws SQLException {
        // Code pour initialiser la connexion à la base de données
        // et instancier le GenericDAO avec la table appropriée
        connection = DriverManager.getConnection("jdbc:h2:mem:testdb", "sa", "");
        empDAO = new GenericDAO<>(connection, "employees", "id");
        
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("CREATE TABLE employees (id INT PRIMARY KEY, nom VARCHAR(255), prenom VARCHAR(255))");
            stmt.execute("INSERT INTO employees (id, nom, prenom) VALUES (1, 'Dupont', 'Jean')");
        }
    }

    @AfterAll
    public static void tearDown() throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("DROP TABLE employees");
        }
        connection.close();
    }

    @Test
    public void testUpdateFields() throws SQLException {
        Map<String, Object> fields = new HashMap<>();
        fields.put("nom", "Durand");
        fields.put("prenom", "Jacques");

        boolean updated = empDAO.updateFields(1, fields);

        assertTrue(updated);

        try (PreparedStatement stmt = connection.prepareStatement("SELECT * FROM employees WHERE id=?")) {
            stmt.setInt(1, 1);
            try (ResultSet rs = stmt.executeQuery()) {
                assertTrue(rs.next());
                assertEquals("Durand", rs.getString("nom"));
                assertEquals("Jacques", rs.getString("prenom"));
            }
        }
    }
}