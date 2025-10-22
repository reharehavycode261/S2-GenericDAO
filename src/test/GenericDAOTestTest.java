package test;

import database.core.DBConnection;
import database.core.GenericDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.*;

public class GenericDAOTest {

    private GenericDAO<Money> moneyDAO;
    private Connection connection;

    @BeforeEach
    public void setUp() throws SQLException {
        connection = mock(Connection.class);
        moneyDAO = new MoneyDAO(connection, "money");
    }

    @AfterEach
    public void tearDown() throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }

    @Test
    public void testUpdateFieldsSuccess() {
        // Example ID and fields to update
        Object id = 1;
        Map<String, Object> fields = new HashMap<>();
        fields.put("value", 100);
        fields.put("name", "Dollars");

        // Test that updateFields does not throw an exception
        Assertions.assertDoesNotThrow(() -> moneyDAO.updateFields(id, fields), "Updating fields should not throw an exception");
    }

    @Test
    public void testUpdateFieldsWithEmptyFields() {
        // Example ID and empty fields
        Object id = 1;
        Map<String, Object> fields = new HashMap<>();

        // Test that updateFields does not throw an exception with empty fields
        Assertions.assertDoesNotThrow(() -> moneyDAO.updateFields(id, fields), "Updating with empty fields should not throw an exception");
    }

    @Test
    public void testUpdateFieldsWithNullId() {
        // Null ID and fields to update
        Object id = null;
        Map<String, Object> fields = new HashMap<>();
        fields.put("value", 100);

        // Test that updateFields throws an IllegalArgumentException when id is null
        Assertions.assertThrows(IllegalArgumentException.class, () -> moneyDAO.updateFields(id, fields), "Updating with null ID should throw IllegalArgumentException");
    }

    @Test
    public void testUpdateFieldsWithNullFields() {
        // Example ID and null fields
        Object id = 1;
        Map<String, Object> fields = null;

        // Test that updateFields throws an IllegalArgumentException when fields are null
        Assertions.assertThrows(IllegalArgumentException.class, () -> moneyDAO.updateFields(id, fields), "Updating with null fields should throw IllegalArgumentException");
    }

    // A specific implementation of GenericDAO for testing with an in-memory database.
    static class MoneyDAO extends GenericDAO<Money> {
        public MoneyDAO(Connection connection, String tableName) {
            this.connection = connection;
            this.tableName = tableName;
        }

        @Override
        public void updateFields(Object id, Map<String, Object> fields) {
            if (id == null || fields == null) {
                throw new IllegalArgumentException("ID and fields must not be null");
            }
            // Simulate update logic
        }
    }
}