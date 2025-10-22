package test;

import database.core.DBConnection;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.junit.Assert.*;

public class DBConnectionTest {
    private DBConnection dbConnection;

    @Before
    public void setUp() throws SQLException {
        Connection connection = DriverManager.getConnection("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1");
        this.dbConnection = new DBConnection(connection);
        // Setup initial test table
        dbConnection.getConnection().createStatement().execute(
            "CREATE TABLE test_table (id INT PRIMARY KEY, name VARCHAR(255), age INT)");
        dbConnection.getConnection().createStatement().execute(
            "INSERT INTO test_table (id, name, age) VALUES (1, 'John Doe', 30)");
    }

    @Test
    public void testUpdateFields() throws SQLException {
        dbConnection.updateFields("test_table", new String[]{"name", "age"}, new Object[]{"Jane Doe", 28}, "id = 1");

        var rs = dbConnection.getConnection().prepareStatement("SELECT name, age FROM test_table WHERE id = 1").executeQuery();
        if (rs.next()) {
            assertEquals("Jane Doe", rs.getString("name"));
            assertEquals(28, rs.getInt("age"));
        }
    }
}