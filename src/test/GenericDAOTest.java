package test;

import database.core.GenericDAO;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class GenericDAOTest {
    private GenericDAO<Object> genericDAO;
    private Connection connection;

    @Before
    public void setUp() throws SQLException {
        connection = DriverManager.getConnection("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1", "sa", "");
        genericDAO = new GenericDAO<>();
        genericDAO.connection = connection;
        genericDAO.tableName = "test_table";

        connection.createStatement().execute("CREATE TABLE test_table (id INT PRIMARY KEY, name VARCHAR(255), age INT)");
        connection.createStatement().execute("INSERT INTO test_table (id, name, age) VALUES (1, 'John Doe', 30)");
    }

    @Test
    public void testUpdateFields() throws SQLException {
        Map<String, Object> fieldsToUpdate = new HashMap<>();
        fieldsToUpdate.put("name", "Jane Doe");
        fieldsToUpdate.put("age", 31);

        Map<String, Object> conditions = new HashMap<>();
        conditions.put("id", 1);

        genericDAO.updateFields(fieldsToUpdate, conditions);

        var resultSet = connection.createStatement().executeQuery("SELECT name, age FROM test_table WHERE id = 1");
        assertTrue(resultSet.next());
        assertEquals("Jane Doe", resultSet.getString("name"));
        assertEquals(31, resultSet.getInt("age"));
    }

    // ... autres tests ...
}