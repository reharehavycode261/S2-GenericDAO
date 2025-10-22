package test;

import database.core.DBConnection;
import database.core.GenericDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import static org.mockito.Mockito.*;

public class GenericDAOTest {
    private DBConnection dbConnection;
    private GenericDAO<Object> genericDAO;
    private Connection connection;

    @BeforeEach
    public void setUp() throws SQLException {
        connection = DriverManager.getConnection("jdbc:h2:mem:testdb", new Properties());
        dbConnection = new DBConnection(null, connection);
        genericDAO = new GenericDAO<>();
    }

    @AfterEach
    public void tearDown() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

    /**
     * Test that updateFields successfully updates fields without throwing exceptions.
     */
    @Test
    public void testUpdateFieldsSuccess() {
        String[] fields = {"name", "age"};
        Object[] values = {"John Doe", 30};
        String condition = "id = 1";

        Assertions.assertDoesNotThrow(() -> {
            genericDAO.updateFields(dbConnection, fields, values, condition);
        }, "updateFields should not throw an exception for valid input");
    }

    /**
     * Test that updateFields throws IllegalArgumentException when fields and values length mismatch.
     */
    @Test
    public void testUpdateFieldsMismatchLength() {
        String[] fields = {"name", "age"};
        Object[] values = {"John Doe"};

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            genericDAO.updateFields(dbConnection, fields, values, "id = 1");
        }, "updateFields should throw IllegalArgumentException when fields and values length mismatch");
    }

    /**
     * Test that updateFields throws SQLException when the database connection is invalid.
     */
    @Test
    public void testUpdateFieldsWithInvalidConnection() throws SQLException {
        String[] fields = {"name", "age"};
        Object[] values = {"John Doe", 30};
        String condition = "id = 1";

        // Close the connection to simulate an invalid connection
        connection.close();

        Assertions.assertThrows(SQLException.class, () -> {
            genericDAO.updateFields(dbConnection, fields, values, condition);
        }, "updateFields should throw SQLException when the database connection is invalid");
    }

    /**
     * Test that updateFields throws NullPointerException when fields are null.
     */
    @Test
    public void testUpdateFieldsWithNullFields() {
        Object[] values = {"John Doe", 30};
        String condition = "id = 1";

        Assertions.assertThrows(NullPointerException.class, () -> {
            genericDAO.updateFields(dbConnection, null, values, condition);
        }, "updateFields should throw NullPointerException when fields are null");
    }

    /**
     * Test that updateFields throws NullPointerException when values are null.
     */
    @Test
    public void testUpdateFieldsWithNullValues() {
        String[] fields = {"name", "age"};
        String condition = "id = 1";

        Assertions.assertThrows(NullPointerException.class, () -> {
            genericDAO.updateFields(dbConnection, fields, null, condition);
        }, "updateFields should throw NullPointerException when values are null");
    }

    /**
     * Test that updateFields throws NullPointerException when condition is null.
     */
    @Test
    public void testUpdateFieldsWithNullCondition() {
        String[] fields = {"name", "age"};
        Object[] values = {"John Doe", 30};

        Assertions.assertThrows(NullPointerException.class, () -> {
            genericDAO.updateFields(dbConnection, fields, values, null);
        }, "updateFields should throw NullPointerException when condition is null");
    }
}