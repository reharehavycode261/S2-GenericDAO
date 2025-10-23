package test;

import database.core.GenericDAO;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GenericDAOTest {

    // Mock de la connexion
    private final GenericDAO<?> mockDAO = mock(GenericDAO.class);

    @Test
    void testUpdateFields() throws SQLException {
        // Préparation des données test
        Map<String, Object> fieldsToUpdate = new HashMap<>();
        fieldsToUpdate.put("nom", "NouveauNom");
        fieldsToUpdate.put("prenom", "NouveauPrenom");

        // Appel de la méthode à tester
        assertDoesNotThrow(() -> mockDAO.updateFields(1, fieldsToUpdate));

        // Vérification de l'appel de la méthode
        verify(mockDAO, times(1)).updateFields(1, fieldsToUpdate);
    }

    @Test
    void testUpdateFieldsWithEmptyFieldsShouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> mockDAO.updateFields(1, new HashMap<>()));
    }
}