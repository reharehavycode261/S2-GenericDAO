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

        assertTrue(updated, "The updateFields method should return true for a successful update.");

        try (PreparedStatement stmt = connection.prepareStatement("SELECT * FROM employees WHERE id=?")) {
            stmt.setInt(1, 1);
            try (ResultSet rs = stmt.executeQuery()) {
                assertTrue(rs.next(), "ResultSet should have a next element after update.");
                assertEquals("Durand", rs.getString("nom"), "The 'nom' field should be updated to 'Durand'.");
                assertEquals("Jacques", rs.getString("prenom"), "The 'prenom' field should be updated to 'Jacques'.");
            }
        }
    }

    @Test
    public void testUpdateFieldsWithNonExistentId() throws SQLException {
        Map<String, Object> fields = new HashMap<>();
        fields.put("nom", "NonExistent");
        fields.put("prenom", "User");

        boolean updated = empDAO.updateFields(999, fields);

        assertFalse(updated, "The updateFields method should return false when updating a non-existent ID.");
    }

    @Test
    public void testUpdateFieldsWithEmptyFields() throws SQLException {
        Map<String, Object> fields = new HashMap<>();

        boolean updated = empDAO.updateFields(1, fields);

        assertFalse(updated, "The updateFields method should return false when no fields are provided for update.");
    }

    @Test
    public void testUpdateFieldsWithNullFields() {
        assertThrows(NullPointerException.class, () -> {
            empDAO.updateFields(1, null);
        }, "The updateFields method should throw NullPointerException when fields map is null.");
    }

    @Test
    public void testUpdateFieldsWithInvalidFieldName() throws SQLException {
        Map<String, Object> fields = new HashMap<>();
        fields.put("invalidField", "Value");

        SQLException exception = assertThrows(SQLException.class, () -> {
            empDAO.updateFields(1, fields);
        }, "The updateFields method should throw SQLException when an invalid field name is provided.");

        assertTrue(exception.getMessage().contains("invalidField"), "Exception message should contain the invalid field name.");
    }
}