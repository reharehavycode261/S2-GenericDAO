package test;

import database.core.DBConnection;
import database.core.GenericDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GenericDAOTest {

    private GenericDAO<Money> moneyDAO;
    private Connection connection;

    @BeforeEach
    public void setUp() throws SQLException {
        connection = DBConnection.getConnection();
        moneyDAO = new MoneyDAO(connection, "money");
    }

    @Test
    public void testUpdateFields() {
        // Example ID and fields to update
        Object id = 1;
        Map<String, Object> fields = new HashMap<>();
        fields.put("value", 100);
        fields.put("name", "Dollars");

        assertDoesNotThrow(() -> moneyDAO.updateFields(id, fields), "Updating fields should not throw an exception");
        
        // Additional validation can be done here if necessary.
    }

    // A specific implementation of GenericDAO for testing with an in-memory database.
    static class MoneyDAO extends GenericDAO<Money> {
        public MoneyDAO(Connection connection, String tableName) {
            this.connection = connection;
            this.tableName = tableName;
        }
    }
}