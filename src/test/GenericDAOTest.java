package test;

import database.core.DBConnection;
import database.core.Database;
import database.core.GenericDAO;
import database.exception.SQL.AttributeMissingException;
import database.provider.PostgreSQL;

import org.junit.Before;
import org.junit.Test;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class GenericDAOTest {
    private GenericDAO<Emp> genericDAO;
    private DBConnection dbConnection;

    @Before
    public void setUp() throws Exception {
        Database database = new PostgreSQL("localhost", "5432", "dao", "", "");
        dbConnection = database.createConnection();
        genericDAO = new GenericDAO<>();
    }

    @Test
    public void testUpdateFieldsSuccess() throws Exception {
        Emp emp = new Emp(); // Assume we have an employee with ID already set
        emp.setId(1); // Example ID

        Map<String, Object> fieldsToUpdate = new HashMap<>();
        fieldsToUpdate.put("name", "John Doe");
        fieldsToUpdate.put("salary", 50000);

        genericDAO.updateFields(dbConnection, emp, fieldsToUpdate);

        // Fetching the same employee to check if updates were successful
        Emp updatedEmp = // logic to fetch from database, assuming a method exists
        assertEquals("John Doe", updatedEmp.getName());
        assertEquals(50000, updatedEmp.getSalary());
    }

    @Test(expected = AttributeMissingException.class)
    public void testUpdateFieldsNoRowUpdated() throws Exception {
        Emp emp = new Emp();
        emp.setId(999); // Assume this ID doesn't exist

        Map<String, Object> fieldsToUpdate = new HashMap<>();
        fieldsToUpdate.put("name", "Test Fail");

        genericDAO.updateFields(dbConnection, emp, fieldsToUpdate);
    }

    @Test(expected = NoSuchFieldException.class)
    public void testUpdateFieldsInvalidField() throws Exception {
        Emp emp = new Emp();
        emp.setId(1);

        Map<String, Object> fieldsToUpdate = new HashMap<>();
        fieldsToUpdate.put("nonExistentField", "value");

        genericDAO.updateFields(dbConnection, emp, fieldsToUpdate);
    }
}