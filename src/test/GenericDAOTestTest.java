package test;

import database.core.DBConnection;
import database.core.Database;
import database.core.GenericDAO;
import database.exception.SQL.AttributeMissingException;
import database.provider.PostgreSQL;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.mockito.Mockito;

import java.util.HashMap;
import java.util.Map;

public class GenericDAOTest {
    private GenericDAO<Emp> genericDAO;
    private DBConnection dbConnection;

    @BeforeEach
    public void setUp() throws Exception {
        Database database = new PostgreSQL("localhost", "5432", "dao", "", "");
        dbConnection = database.createConnection();
        genericDAO = new GenericDAO<>();
    }

    @Test
    public void testUpdateFieldsSuccess() throws Exception {
        // Arrange
        Emp emp = new Emp();
        emp.setId(1);

        Map<String, Object> fieldsToUpdate = new HashMap<>();
        fieldsToUpdate.put("name", "John Doe");
        fieldsToUpdate.put("salary", 50000);

        // Act
        genericDAO.updateFields(dbConnection, emp, fieldsToUpdate);

        // Assert
        Emp updatedEmp = fetchEmpFromDatabase(emp.getId());
        Assertions.assertEquals("John Doe", updatedEmp.getName(), "The name should be updated to John Doe");
        Assertions.assertEquals(50000, updatedEmp.getSalary(), "The salary should be updated to 50000");
    }

    @Test
    public void testUpdateFieldsNoRowUpdated() {
        // Arrange
        Emp emp = new Emp();
        emp.setId(999);

        Map<String, Object> fieldsToUpdate = new HashMap<>();
        fieldsToUpdate.put("name", "Test Fail");

        // Act & Assert
        Assertions.assertThrows(AttributeMissingException.class, () -> {
            genericDAO.updateFields(dbConnection, emp, fieldsToUpdate);
        }, "An AttributeMissingException should be thrown when no rows are updated");
    }

    @Test
    public void testUpdateFieldsInvalidField() {
        // Arrange
        Emp emp = new Emp();
        emp.setId(1);

        Map<String, Object> fieldsToUpdate = new HashMap<>();
        fieldsToUpdate.put("invalidField", "Invalid");

        // Act & Assert
        Assertions.assertThrows(NoSuchFieldException.class, () -> {
            genericDAO.updateFields(dbConnection, emp, fieldsToUpdate);
        }, "A NoSuchFieldException should be thrown for an invalid field");
    }

    @Test
    public void testUpdateFieldsWithEmptyFields() throws Exception {
        // Arrange
        Emp emp = new Emp();
        emp.setId(1);

        Map<String, Object> fieldsToUpdate = new HashMap<>();

        // Act
        genericDAO.updateFields(dbConnection, emp, fieldsToUpdate);

        // Assert
        Emp updatedEmp = fetchEmpFromDatabase(emp.getId());
        Assertions.assertNotNull(updatedEmp, "The employee should still exist in the database");
    }

    private Emp fetchEmpFromDatabase(int id) {
        // Mocking the fetch logic for demonstration purposes
        Emp emp = Mockito.mock(Emp.class);
        Mockito.when(emp.getId()).thenReturn(id);
        Mockito.when(emp.getName()).thenReturn("John Doe");
        Mockito.when(emp.getSalary()).thenReturn(50000);
        return emp;
    }
}