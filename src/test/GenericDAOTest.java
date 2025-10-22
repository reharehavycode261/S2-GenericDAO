package test;

import database.core.DBConnection;
import database.core.GenericDAO;
import database.provider.PostgreSQL;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class GenericDAOTest {
    private GenericDAO<Emp> empDao;
    private Connection connection;

    @BeforeEach
    public void setUp() throws Exception {
        PostgreSQL database = new PostgreSQL("localhost", "5432", "dao", "user", "password");
        DBConnection dbConnection = database.createConnection();
        connection = dbConnection.getConnection();
        empDao = new GenericDAO<>(connection, "Employee");
    }

    @Test
    public void testUpdateFields() throws Exception {
        // Simulate updating a field
        Map<String, Object> fields = new HashMap<>();
        fields.put("nom", "Pierre");
        
        String whereClause = "prenom = 'Jean'";

        empDao.updateFields(fields, whereClause);

        // Assuming a method to fetch the updated value to assert exists
        // For the test, this would ensure the "nom" is updated to "Pierre" where "prenom" is "Jean"
        // Use assertions to verify the update
        // assertEquals("Pierre", fetchedValue);
    }

    @Test
    public void testUpdateFieldsWithEmptyFields() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            empDao.updateFields(new HashMap<>(), "prenom = 'Jean'");
        });

        String expectedMessage = "Fields to update cannot be null or empty";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testUpdateFieldsWithNullWhereClause() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            Map<String, Object> fields = new HashMap<>();
            fields.put("nom", "Pierre");
            empDao.updateFields(fields, null);
        });

        String expectedMessage = "Where clause cannot be null or empty";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }
}