package test;

import database.core.DBConnection;
import database.core.GenericDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class GenericDAOTest {
    private DBConnection dbConnection;
    private GenericDAO<Object> genericDAO;

    @BeforeEach
    public void setUp() throws SQLException {
        Connection connection = DriverManager.getConnection("jdbc:h2:mem:testdb", new Properties());
        dbConnection = new DBConnection(null, connection);
        genericDAO = new GenericDAO<>();
    }

    @Test
    public void testUpdateFieldsSuccess() {
        String[] fields = {"name", "age"};
        Object[] values = {"John Doe", 30};
        String condition = "id = 1";

        assertDoesNotThrow(() -> {
            genericDAO.updateFields(dbConnection, fields, values, condition);
        });
    }

    @Test
    public void testUpdateFieldsMismatchLength() {
        String[] fields = {"name", "age"};
        Object[] values = {"John Doe"};

        assertThrows(IllegalArgumentException.class, () -> {
            genericDAO.updateFields(dbConnection, fields, values, "id = 1");
        });
    }
}