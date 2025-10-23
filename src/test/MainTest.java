import database.core.GenericDAO;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Arrays;

public class MainTest {

    private Connection connection;
    private GenericDAO<Emp> empDao;

    @Before
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

        // Additional code would be required to retrieve the updated entity and assert its properties
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