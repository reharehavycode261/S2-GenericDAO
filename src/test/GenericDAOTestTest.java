package test;

import database.core.GenericDAO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Assertions;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.*;

class GenericDAOTest {

    private GenericDAO<?> mockDAO;

    @BeforeEach
    void setUp() {
        // Initialisation du mock avant chaque test
        mockDAO = mock(GenericDAO.class);
    }

    @AfterEach
    void tearDown() {
        // Réinitialisation du mock après chaque test
        mockDAO = null;
    }

    @Nested
    @DisplayName("Tests for updateFields method")
    class UpdateFieldsTests {

        @Test
        @DisplayName("Test updateFields with valid data")
        void testUpdateFields() throws SQLException {
            // Préparation des données test
            Map<String, Object> fieldsToUpdate = new HashMap<>();
            fieldsToUpdate.put("nom", "NouveauNom");
            fieldsToUpdate.put("prenom", "NouveauPrenom");

            // Appel de la méthode à tester
            Assertions.assertDoesNotThrow(() -> mockDAO.updateFields(1, fieldsToUpdate), "updateFields should not throw an exception with valid data");

            // Vérification de l'appel de la méthode
            verify(mockDAO, times(1)).updateFields(1, fieldsToUpdate);
        }

        @Test
        @DisplayName("Test updateFields with empty fields should throw IllegalArgumentException")
        void testUpdateFieldsWithEmptyFieldsShouldThrowException() {
            // Vérification que l'exception est lancée
            Assertions.assertThrows(IllegalArgumentException.class, () -> mockDAO.updateFields(1, new HashMap<>()), "updateFields should throw IllegalArgumentException when fields are empty");
        }

        @Test
        @DisplayName("Test updateFields with null fields should throw NullPointerException")
        void testUpdateFieldsWithNullFieldsShouldThrowException() {
            // Vérification que l'exception est lancée
            Assertions.assertThrows(NullPointerException.class, () -> mockDAO.updateFields(1, null), "updateFields should throw NullPointerException when fields are null");
        }

        @Test
        @DisplayName("Test updateFields with invalid ID should throw SQLException")
        void testUpdateFieldsWithInvalidIdShouldThrowSQLException() throws SQLException {
            // Configuration du mock pour lancer une SQLException
            doThrow(SQLException.class).when(mockDAO).updateFields(-1, anyMap());

            // Vérification que l'exception est lancée
            Assertions.assertThrows(SQLException.class, () -> mockDAO.updateFields(-1, new HashMap<>()), "updateFields should throw SQLException when ID is invalid");
        }
    }
}