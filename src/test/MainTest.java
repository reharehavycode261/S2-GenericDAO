package test;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.mockito.Mockito;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

// Importer les classes nécessaires pour le mock
import static org.mockito.Mockito.*;

public class MainTest {

    private Connection mockConnection;
    private PlatDAO platDAO;

    @BeforeEach
    public void setUp() throws SQLException {
        // Créer un mock de la connexion
        mockConnection = Mockito.mock(Connection.class);
        platDAO = new PlatDAO(mockConnection);
    }

    @AfterEach
    public void tearDown() throws SQLException {
        // Fermer la connexion mockée si nécessaire
        mockConnection.close();
    }

    @Test
    public void testCountAllRecords() throws SQLException {
        // Mock du Statement et ResultSet
        Statement mockStatement = Mockito.mock(Statement.class);
        ResultSet mockResultSet = Mockito.mock(ResultSet.class);

        // Configurer les comportements attendus
        when(mockConnection.createStatement()).thenReturn(mockStatement);
        when(mockStatement.executeQuery("SELECT COUNT(*) FROM Plat")).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getLong(1)).thenReturn(10L);

        // Appeler la méthode à tester
        long count = platDAO.count(null);

        // Vérifier le résultat
        Assertions.assertEquals(10L, count, "Le nombre total d'enregistrements devrait être 10");
    }

    @Test
    public void testCountWithCondition() throws SQLException {
        // Mock du Statement et ResultSet
        Statement mockStatement = Mockito.mock(Statement.class);
        ResultSet mockResultSet = Mockito.mock(ResultSet.class);

        // Configurer les comportements attendus
        when(mockConnection.createStatement()).thenReturn(mockStatement);
        when(mockStatement.executeQuery("SELECT COUNT(*) FROM Plat WHERE libelle LIKE 'Salad%'")).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getLong(1)).thenReturn(5L);

        // Appeler la méthode à tester
        long count = platDAO.count("libelle LIKE 'Salad%'");

        // Vérifier le résultat
        Assertions.assertEquals(5L, count, "Le nombre d'enregistrements avec la condition devrait être 5");
    }

    @Test
    public void testSQLExceptionHandling() throws SQLException {
        // Configurer le mock pour lancer une SQLException
        when(mockConnection.createStatement()).thenThrow(new SQLException("Erreur de connexion"));

        // Vérifier que l'exception est bien lancée
        Assertions.assertThrows(SQLException.class, () -> {
            platDAO.count(null);
        }, "Une SQLException devrait être lancée en cas d'erreur de connexion");
    }
}