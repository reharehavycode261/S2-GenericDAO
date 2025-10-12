package test;

import database.core.GenericDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

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

    @AfterEach
    public void tearDown() throws SQLException {
        // Nettoyage de la base de données après chaque test
        Statement stmt = connection.createStatement();
        stmt.execute("DROP TABLE Emp");
        connection.close();
    }

    @Test
    public void testCount() throws SQLException {
        // Test positif: Vérifie que le nombre d'enregistrements est correct
        long count = empDAO.count();
        Assertions.assertEquals(2, count, "The count method did not return the correct number of entries.");
    }

    @Test
    public void testCountWithNoRecords() throws SQLException {
        // Test négatif: Vérifie le comportement lorsque la table est vide
        Statement stmt = connection.createStatement();
        stmt.execute("DELETE FROM Emp");
        
        long count = empDAO.count();
        Assertions.assertEquals(0, count, "The count method did not return zero for an empty table.");
    }

    @Test
    public void testCountWithException() {
        // Test négatif: Vérifie le comportement en cas d'exception SQL
        Assertions.assertThrows(SQLException.class, () -> {
            Connection invalidConnection = DriverManager.getConnection("jdbc:h2:mem:invalid", "sa", "");
            GenericDAO<Emp> invalidDAO = new GenericDAO<>(invalidConnection, "InvalidTable");
            invalidDAO.count();
        }, "Expected count() to throw, but it didn't");
    }
}