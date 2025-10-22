import database.core.DBConnection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

public class DBConnectionTest {
    
    private DBConnection dbConnection;

    @BeforeEach
    public void setup() throws SQLException {
        // Assurez-vous de remplacer par une connexion de test valide
        Connection connection = DriverManager.getConnection("jdbc:h2:mem:testdb", "sa", "");
        dbConnection = new DBConnection(connection);

        // Set up the database schema for testing - Example
        connection.createStatement().execute("CREATE TABLE test_table (id INT PRIMARY KEY, name VARCHAR(255), age INT)");
        connection.createStatement().execute("INSERT INTO test_table (id, name, age) VALUES (1, 'John Doe', 30)");
    }

    @Test
    public void testUpdateFields() throws SQLException {
        dbConnection.updateFields("test_table", new String[]{"name", "age"}, new Object[]{"Jane Doe", 25}, "id = 1");

        // Check if the update was successful
        var resultSet = dbConnection.connection.createStatement().executeQuery("SELECT name, age FROM test_table WHERE id = 1");
        assertTrue(resultSet.next());
        assertEquals("Jane Doe", resultSet.getString("name"));
        assertEquals(25, resultSet.getInt("age"));
    }
}