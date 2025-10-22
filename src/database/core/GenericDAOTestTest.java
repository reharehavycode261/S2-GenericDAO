package database.core;

import org.junit.jupiter.api.*;
import java.sql.*;
import static org.junit.jupiter.api.Assertions.*;

class GenericDAOTest {

    private Connection connection;
    private GenericDAO<Object> dao;

    @BeforeEach
    void setUp() throws SQLException {
        // Assurer que la connexion est établie ici
        this.connection = DriverManager.getConnection("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;");
        this.dao = new GenericDAO<>(connection, "TestTable");

        // Créer une table de test pour la validation
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(
                "CREATE TABLE TestTable (" +
                "id INT PRIMARY KEY, " +
                "field1 VARCHAR(255), " +
                "field2 INT)"
            );
            stmt.execute("INSERT INTO TestTable VALUES (1, 'value1', 100)");
        }
    }

    @Test
    void testUpdateFields() throws SQLException {
        // Test positif: mise à jour réussie des champs
        String[] fields = {"field1", "field2"};
        Object[] values = {"newValue", 200};

        dao.updateFields(fields, values, "id = 1");

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT field1, field2 FROM TestTable WHERE id = 1")) {
            assertTrue(rs.next(), "Row with id=1 should exist");
            assertEquals("newValue", rs.getString("field1"), "field1 should be updated to 'newValue'");
            assertEquals(200, rs.getInt("field2"), "field2 should be updated to 200");
        }
    }

    @Test
    void testUpdateFieldsWithNonExistingRow() throws SQLException {
        // Test négatif: tentative de mise à jour d'une ligne inexistante
        String[] fields = {"field1", "field2"};
        Object[] values = {"newValue", 200};

        dao.updateFields(fields, values, "id = 999");

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT field1, field2 FROM TestTable WHERE id = 999")) {
            assertFalse(rs.next(), "Row with id=999 should not exist");
        }
    }

    @Test
    void testUpdateFieldsWithInvalidField() {
        // Test négatif: tentative de mise à jour avec un champ invalide
        String[] fields = {"invalidField"};
        Object[] values = {"newValue"};

        SQLException exception = assertThrows(SQLException.class, () -> {
            dao.updateFields(fields, values, "id = 1");
        });

        assertNotNull(exception.getMessage(), "Exception message should not be null");
    }

    @AfterEach
    void tearDown() throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("DROP TABLE TestTable");
        }
        connection.close();
    }
}