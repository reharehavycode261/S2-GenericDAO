import database.core.GenericDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

public class MainTest {

    private Connection connection;
    private GenericDAO<Emp> empDao;

    @BeforeEach
    public void setup() throws SQLException {
        String url = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1"; // H2 in-memory database for testing
        connection = DriverManager.getConnection(url);
        empDao = new GenericDAO<>("Emp", connection);

        connection.createStatement().execute("CREATE TABLE Emp (id INT PRIMARY KEY, nom VARCHAR, prenom VARCHAR)");
        connection.createStatement().execute("INSERT INTO Emp (id, nom, prenom) VALUES (1, 'Doe', 'John')");
    }

    @Test
    public void testUpdateFields() throws SQLException {
        Emp emp = new Emp();
        emp.setId(1);
        emp.setNom("Smith");
        emp.setPrenom("Jane");

        empDao.updateFields(emp, Arrays.asList("nom", "prenom"));

        // Retrieve the updated entity and assert its properties
        ResultSet rs = connection.createStatement().executeQuery("SELECT nom, prenom FROM Emp WHERE id = 1");
        if (rs.next()) {
            Assertions.assertEquals("Smith", rs.getString("nom"), "The last name should be updated to Smith");
            Assertions.assertEquals("Jane", rs.getString("prenom"), "The first name should be updated to Jane");
        } else {
            Assertions.fail("No record found with id 1");
        }
    }

    @Test
    public void testGetId() {
        Emp emp = new Emp();
        emp.setId(1);
        Assertions.assertEquals(1, emp.getId(), "The ID should be 1");
    }

    @Test
    public void testSetId() {
        Emp emp = new Emp();
        emp.setId(2);
        Assertions.assertEquals(2, emp.getId(), "The ID should be set to 2");
    }

    @Test
    public void testGetNom() {
        Emp emp = new Emp();
        emp.setNom("Doe");
        Assertions.assertEquals("Doe", emp.getNom(), "The last name should be Doe");
    }

    @Test
    public void testSetNom() {
        Emp emp = new Emp();
        emp.setNom("Smith");
        Assertions.assertEquals("Smith", emp.getNom(), "The last name should be set to Smith");
    }

    @Test
    public void testGetPrenom() {
        Emp emp = new Emp();
        emp.setPrenom("John");
        Assertions.assertEquals("John", emp.getPrenom(), "The first name should be John");
    }

    @Test
    public void testSetPrenom() {
        Emp emp = new Emp();
        emp.setPrenom("Jane");
        Assertions.assertEquals("Jane", emp.getPrenom(), "The first name should be set to Jane");
    }

    @Test
    public void testUpdateFieldsWithNonExistentId() {
        Emp emp = new Emp();
        emp.setId(999);
        emp.setNom("NonExistent");
        emp.setPrenom("User");

        Assertions.assertThrows(SQLException.class, () -> {
            empDao.updateFields(emp, Arrays.asList("nom", "prenom"));
        }, "Updating a non-existent ID should throw SQLException");
    }

    @Test
    public void testUpdateFieldsWithEmptyFieldsList() throws SQLException {
        Emp emp = new Emp();
        emp.setId(1);
        emp.setNom("NoChange");
        emp.setPrenom("NoChange");

        empDao.updateFields(emp, Arrays.asList());

        ResultSet rs = connection.createStatement().executeQuery("SELECT nom, prenom FROM Emp WHERE id = 1");
        if (rs.next()) {
            Assertions.assertEquals("Doe", rs.getString("nom"), "The last name should remain Doe");
            Assertions.assertEquals("John", rs.getString("prenom"), "The first name should remain John");
        } else {
            Assertions.fail("No record found with id 1");
        }
    }
}

class Emp {
    private int id;
    private String nom;
    private String prenom;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }
}