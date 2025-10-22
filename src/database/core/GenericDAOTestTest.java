package database.core;

import org.junit.jupiter.api.*;
import java.sql.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class GenericDAOTest {

    private Connection connection;
    private GenericDAO<TestEntity> dao;

    @BeforeEach
    public void setUp() throws SQLException {
        connection = DriverManager.getConnection("jdbc:h2:mem:testdb", "sa", "");
        Statement stmt = connection.createStatement();
        stmt.execute("CREATE TABLE test_entity (id INT PRIMARY KEY, name VARCHAR(255), age INT)");
        dao = new GenericDAO<>(connection, "test_entity");
    }

    @AfterEach
    public void tearDown() throws SQLException {
        Statement stmt = connection.createStatement();
        stmt.execute("DROP TABLE test_entity");
        connection.close();
    }

    @Test
    public void testUpdateFields() throws SQLException {
        Statement stmt = connection.createStatement();
        stmt.execute("INSERT INTO test_entity (id, name, age) VALUES (1, 'John Doe', 30)");

        Map<String, Object> fieldsToUpdate = new HashMap<>();
        fieldsToUpdate.put("name", "Jane Doe");
        fieldsToUpdate.put("age", 31);

        dao.updateFields(1, fieldsToUpdate);

        ResultSet rs = stmt.executeQuery("SELECT * FROM test_entity WHERE id = 1");
        if (rs.next()) {
            assertEquals("Jane Doe", rs.getString("name"));
            assertEquals(31, rs.getInt("age"));
        } else {
            fail("Enregistrement non trouvé après mise à jour.");
        }
    }

    @Test
    public void testUpdateFieldsWithNonExistentId() throws SQLException {
        Map<String, Object> fieldsToUpdate = new HashMap<>();
        fieldsToUpdate.put("name", "Jane Doe");
        fieldsToUpdate.put("age", 31);

        SQLException exception = assertThrows(SQLException.class, () -> {
            dao.updateFields(999, fieldsToUpdate);
        });

        assertTrue(exception.getMessage().contains("No data found"), "Exception message should indicate no data found.");
    }

    @Test
    public void testUpdateFieldsWithEmptyFields() throws SQLException {
        Statement stmt = connection.createStatement();
        stmt.execute("INSERT INTO test_entity (id, name, age) VALUES (1, 'John Doe', 30)");

        Map<String, Object> fieldsToUpdate = new HashMap<>();

        dao.updateFields(1, fieldsToUpdate);

        ResultSet rs = stmt.executeQuery("SELECT * FROM test_entity WHERE id = 1");
        if (rs.next()) {
            assertEquals("John Doe", rs.getString("name"));
            assertEquals(30, rs.getInt("age"));
        } else {
            fail("Enregistrement non trouvé après mise à jour.");
        }
    }

    @Test
    public void testUpdateFieldsWithNullFields() throws SQLException {
        Statement stmt = connection.createStatement();
        stmt.execute("INSERT INTO test_entity (id, name, age) VALUES (1, 'John Doe', 30)");

        Map<String, Object> fieldsToUpdate = null;

        assertThrows(NullPointerException.class, () -> {
            dao.updateFields(1, fieldsToUpdate);
        });
    }
}