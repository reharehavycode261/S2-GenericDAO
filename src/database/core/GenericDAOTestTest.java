import org.junit.jupiter.api.*;
import org.mockito.*;

import java.sql.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class GenericDAOTest {

    private Connection mockConnection;
    private GenericDAO<Object> genericDAO;

    @BeforeEach
    public void setUp() {
        mockConnection = mock(Connection.class);
        genericDAO = new GenericDAO<>(mockConnection, "test_table");
    }

    /**
     * Teste la mise à jour réussie des champs.
     * Vérifie que la méthode executeUpdate est appelée et que le résultat est vrai.
     */
    @Test
    public void testUpdateFieldsSuccess() throws SQLException {
        Map<String, Object> fields = new HashMap<>();
        fields.put("name", "John Doe");
        fields.put("email", "johndoe@example.com");

        PreparedStatement mockStatement = mock(PreparedStatement.class);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        when(mockStatement.executeUpdate()).thenReturn(1);

        boolean result = genericDAO.updateFields(1, fields);

        assertTrue(result, "La mise à jour devrait réussir avec un retour true.");
        verify(mockStatement, times(1)).executeUpdate();
    }

    /**
     * Teste l'échec de la mise à jour des champs.
     * Vérifie que la méthode executeUpdate est appelée et que le résultat est faux.
     */
    @Test
    public void testUpdateFieldsFailure() throws SQLException {
        Map<String, Object> fields = new HashMap<>();
        fields.put("name", "John Doe");

        PreparedStatement mockStatement = mock(PreparedStatement.class);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        when(mockStatement.executeUpdate()).thenReturn(0);

        boolean result = genericDAO.updateFields(1, fields);

        assertFalse(result, "La mise à jour devrait échouer avec un retour false.");
        verify(mockStatement, times(1)).executeUpdate();
    }

    /**
     * Teste la mise à jour avec des champs vides.
     * Vérifie que le résultat est faux sans exécuter de mise à jour.
     */
    @Test
    public void testUpdateFieldsWithEmptyFields() throws SQLException {
        boolean result = genericDAO.updateFields(1, new HashMap<>());

        assertFalse(result, "La mise à jour avec des champs vides devrait échouer.");
    }

    /**
     * Teste la mise à jour avec un ID invalide.
     * Vérifie que le résultat est faux et qu'aucune mise à jour n'est exécutée.
     */
    @Test
    public void testUpdateFieldsWithInvalidId() throws SQLException {
        Map<String, Object> fields = new HashMap<>();
        fields.put("name", "John Doe");

        PreparedStatement mockStatement = mock(PreparedStatement.class);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        when(mockStatement.executeUpdate()).thenReturn(0);

        boolean result = genericDAO.updateFields(-1, fields);

        assertFalse(result, "La mise à jour avec un ID invalide devrait échouer.");
        verify(mockStatement, times(0)).executeUpdate();
    }

    /**
     * Teste la gestion des exceptions SQL lors de la mise à jour.
     * Vérifie que l'exception est correctement propagée.
     */
    @Test
    public void testUpdateFieldsSQLException() throws SQLException {
        Map<String, Object> fields = new HashMap<>();
        fields.put("name", "John Doe");

        when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("Erreur SQL"));

        assertThrows(SQLException.class, () -> {
            genericDAO.updateFields(1, fields);
        }, "Une SQLException devrait être lancée en cas d'erreur SQL.");
    }
}