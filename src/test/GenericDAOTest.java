package test;

import database.core.GenericDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GenericDAOTest {
    private GenericDAO<Emp> empDAO;
    private Connection connection;

    @BeforeEach
    public void setUp() throws SQLException {
        // Setup de la connexion à la base de données en mémoire pour les tests
        connection = DriverManager.getConnection("jdbc:h2:mem:testdb", "sa", "");
        Statement stmt = connection.createStatement();
        stmt.execute("CREATE TABLE Emp (id INT PRIMARY KEY, nom VARCHAR(255), prenom VARCHAR(255))");
        stmt.execute("INSERT INTO Emp (id, nom, prenom) VALUES (1, 'John', 'Doe'), (2, 'Jane', 'Doe')");
        
        empDAO = new GenericDAO<>(connection, "Emp");
    }

    @Test
    public void testCount() throws SQLException {
        long count = empDAO.count();
        assertEquals(2, count, "The count method did not return the correct number of entries.");
    }
}