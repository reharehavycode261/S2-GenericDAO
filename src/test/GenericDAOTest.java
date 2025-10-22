package test;

import database.core.GenericDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class GenericDAOTest {
    private GenericDAO<Object> genericDAO;
    private Connection connection;

    @BeforeEach
    public void setUp() throws SQLException {
        connection = DriverManager.getConnection("jdbc:h2:mem:testdb", "sa", "");
        genericDAO = new GenericDAO<>(connection, "your_table_name");
        // You may want to set up your test database schema here
    }

    @Test
    public void testUpdateFields_withValidFields_shouldNotThrowException() throws SQLException {
        Map<String, Object> fields = new HashMap<>();
        fields.put("column1", "value1");
        fields.put("column2", 123);

        // Assuming that '1' is a valid ID in the test database.
        genericDAO.updateFields(1, fields);
    }

    @Test
    public void testUpdateFields_withEmptyFields_shouldThrowIllegalArgumentException() {
        Map<String, Object> fields = new HashMap<>();

        assertThrows(IllegalArgumentException.class, () -> genericDAO.updateFields(1, fields));
    }
}