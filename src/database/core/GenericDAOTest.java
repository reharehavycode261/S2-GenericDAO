package database.core;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.mockito.Mockito;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.mockito.Mockito.*;

public class GenericDAOTest {

    private Connection mockConnection;
    private PreparedStatement mockPreparedStatement;
    private ResultSet mockResultSet;
    private GenericDAO<Object> genericDAO;

    @BeforeEach
    public void setUp() throws SQLException {
        mockConnection = Mockito.mock(Connection.class);
        mockPreparedStatement = Mockito.mock(PreparedStatement.class);
        mockResultSet = Mockito.mock(ResultSet.class);

        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);

        genericDAO = new GenericDAO<>(mockConnection, "test_table");
    }

    @AfterEach
    public void tearDown() throws SQLException {
        mockConnection.close();
    }

    /**
     * Test positif : Vérifie que la méthode count() retourne le bon nombre d'enregistrements.
     */
    @Test
    public void testCountReturnsCorrectNumber() throws SQLException {
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getLong(1)).thenReturn(5L);

        long count = genericDAO.count();

        Assertions.assertEquals(5L, count, "La méthode count() devrait retourner 5.");
    }

    /**
     * Test négatif : Vérifie que la méthode count() retourne 0 si aucun enregistrement n'est présent.
     */
    @Test
    public void testCountReturnsZeroWhenNoRecords() throws SQLException {
        when(mockResultSet.next()).thenReturn(false);

        long count = genericDAO.count();

        Assertions.assertEquals(0L, count, "La méthode count() devrait retourner 0 lorsqu'aucun enregistrement n'est présent.");
    }

    /**
     * Test d'erreur : Vérifie que la méthode count() lève une SQLException en cas d'erreur SQL.
     */
    @Test
    public void testCountThrowsSQLException() throws SQLException {
        when(mockPreparedStatement.executeQuery()).thenThrow(new SQLException("Erreur SQL"));

        Assertions.assertThrows(SQLException.class, () -> {
            genericDAO.count();
        }, "La méthode count() devrait lever une SQLException en cas d'erreur SQL.");
    }
}