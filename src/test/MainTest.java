package test;

import database.core.DBConnection;
import database.core.GenericDAO;
import database.core.Plat;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.mockito.Mockito;

import java.sql.SQLException;

import static org.mockito.Mockito.*;

public class MainTest {

    private DBConnection mockConnection;
    private GenericDAO<Plat> mockPlatDAO;

    @BeforeEach
    public void setUp() {
        // Crée un mock pour DBConnection
        mockConnection = Mockito.mock(DBConnection.class);
        // Crée un mock pour GenericDAO
        mockPlatDAO = Mockito.mock(GenericDAO.class);
    }

    @AfterEach
    public void tearDown() {
        mockConnection = null;
        mockPlatDAO = null;
    }

    @Test
    public void testCountMethodSuccess() throws SQLException {
        // Arrange
        long expectedCount = 10;
        when(mockPlatDAO.count()).thenReturn(expectedCount);

        // Act
        long actualCount = mockPlatDAO.count();

        // Assert
        Assertions.assertEquals(expectedCount, actualCount, "Le nombre d'enregistrements devrait être égal à 10.");
    }

    @Test
    public void testCountMethodThrowsSQLException() throws SQLException {
        // Arrange
        when(mockPlatDAO.count()).thenThrow(new SQLException("Erreur de connexion à la base de données"));

        // Act & Assert
        SQLException thrown = Assertions.assertThrows(SQLException.class, () -> {
            mockPlatDAO.count();
        }, "Une SQLException devrait être lancée en cas d'erreur de connexion.");

        Assertions.assertEquals("Erreur de connexion à la base de données", thrown.getMessage(), "Le message d'erreur devrait être 'Erreur de connexion à la base de données'.");
    }

    @Test
    public void testMainMethodHandlesSQLException() {
        // Arrange
        try {
            when(mockPlatDAO.count()).thenThrow(new SQLException("Erreur de connexion à la base de données"));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Act
        try {
            Main.main(new String[]{});
        } catch (Exception e) {
            Assertions.fail("Main method should handle SQLException without throwing it.");
        }
    }
}