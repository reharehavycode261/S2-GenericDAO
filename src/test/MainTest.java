package test;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.Assertions;
import database.core.Database;
import database.provider.Oracle;
import java.sql.SQLException;

@ExtendWith(MockitoExtension.class)
public class MainTest {

    @Mock
    private Database mockDatabase;
    
    private EmpDAO empDAO;

    @BeforeEach
    void setUp() {
        empDAO = new EmpDAO(mockDatabase);
    }

    @Test
    @DisplayName("Test de la connexion à la base de données Oracle")
    void testDatabaseConnection() {
        Assertions.assertDoesNotThrow(() -> {
            Database database = new Oracle("jdbc:oracle:thin:@localhost:1521:xe", "username", "password");
        }, "La connexion à la base de données devrait se faire sans exception");
    }

    @Test
    @DisplayName("Test du count total des employés")
    void testCountAllEmployees() throws SQLException {
        when(mockDatabase.executeQuery(anyString())).thenReturn(10L);
        
        long count = empDAO.count();
        
        Assertions.assertEquals(10L, count, "Le nombre total d'employés devrait être 10");
        verify(mockDatabase).executeQuery(contains("SELECT COUNT(*) FROM Emp"));
    }

    @Test
    @DisplayName("Test du count avec condition")
    void testCountEmployeesWithCondition() throws SQLException {
        when(mockDatabase.executeQuery(anyString())).thenReturn(5L);
        
        long count = empDAO.count("nom LIKE 'D%'");
        
        Assertions.assertEquals(5L, count, "Le nombre d'employés avec condition devrait être 5");
        verify(mockDatabase).executeQuery(contains("WHERE nom LIKE 'D%'"));
    }

    @Test
    @DisplayName("Test de gestion d'erreur SQL")
    void testSQLException() {
        when(mockDatabase.executeQuery(anyString())).thenThrow(new SQLException("Erreur SQL"));
        
        Assertions.assertThrows(SQLException.class, () -> {
            empDAO.count();
        }, "Une SQLException devrait être levée");
    }

    @Test
    @DisplayName("Test avec paramètres de connexion invalides")
    void testInvalidConnectionParameters() {
        Assertions.assertThrows(Exception.class, () -> {
            Database database = new Oracle("invalid_url", "", "");
        }, "Une exception devrait être levée avec des paramètres de connexion invalides");
    }

    @Test
    @DisplayName("Test avec condition de recherche null")
    void testCountWithNullCondition() throws SQLException {
        when(mockDatabase.executeQuery(anyString())).thenReturn(0L);
        
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            empDAO.count(null);
        }, "Une IllegalArgumentException devrait être levée avec une condition null");
    }

    @Test
    @DisplayName("Test avec condition de recherche vide")
    void testCountWithEmptyCondition() throws SQLException {
        when(mockDatabase.executeQuery(anyString())).thenReturn(0L);
        
        long count = empDAO.count("");
        
        Assertions.assertEquals(0L, count, "Le count avec une condition vide devrait retourner 0");
    }
}