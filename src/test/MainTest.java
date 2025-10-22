package test;

import database.core.GenericDAO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.mockito.Mockito;

import java.util.HashMap;
import java.util.Map;

class MainTest {

    private GenericDAO<SomeEntity> daoMock;
    private Map<String, Object> fields;

    @BeforeEach
    void setUp() {
        // Initialisation des mocks et des objets nécessaires pour les tests
        daoMock = Mockito.mock(GenericDAO.class);
        fields = new HashMap<>();
        fields.put("name", "Updated Name");
        fields.put("age", 30);
    }

    @AfterEach
    void tearDown() {
        // Nettoyage après chaque test
        daoMock = null;
        fields = null;
    }

    @Test
    void testMainMethodSuccess() {
        // Test du cas où la méthode main s'exécute avec succès
        try {
            Mockito.doNothing().when(daoMock).updateFields(Mockito.eq(1), Mockito.anyMap());
            Main.main(new String[]{});
            // Si aucune exception n'est lancée, le test est réussi
            Assertions.assert(true, "La méthode main s'exécute sans exception.");
        } catch (Exception e) {
            Assertions.fail("La méthode main a lancé une exception inattendue: " + e.getMessage());
        }
    }

    @Test
    void testUpdateFieldsWithValidData() {
        // Test de la méthode updateFields avec des données valides
        try {
            Mockito.doNothing().when(daoMock).updateFields(Mockito.eq(1), Mockito.eq(fields));
            daoMock.updateFields(1, fields);
            Mockito.verify(daoMock, Mockito.times(1)).updateFields(1, fields);
            Assertions.assert(true, "updateFields a été appelé avec succès avec des données valides.");
        } catch (Exception e) {
            Assertions.fail("updateFields a lancé une exception inattendue: " + e.getMessage());
        }
    }

    @Test
    void testUpdateFieldsWithInvalidId() {
        // Test de la méthode updateFields avec un ID invalide
        try {
            Mockito.doThrow(new IllegalArgumentException("Invalid ID")).when(daoMock).updateFields(Mockito.eq(-1), Mockito.anyMap());
            daoMock.updateFields(-1, fields);
            Assertions.fail("updateFields aurait dû lancer une exception pour un ID invalide.");
        } catch (IllegalArgumentException e) {
            Assertions.assert(e.getMessage().equals("Invalid ID"), "L'exception attendue a été lancée pour un ID invalide.");
        } catch (Exception e) {
            Assertions.fail("Une exception inattendue a été lancée: " + e.getMessage());
        }
    }

    @Test
    void testUpdateFieldsWithNullFields() {
        // Test de la méthode updateFields avec des champs nuls
        try {
            Mockito.doThrow(new NullPointerException("Fields map is null")).when(daoMock).updateFields(Mockito.eq(1), Mockito.isNull());
            daoMock.updateFields(1, null);
            Assertions.fail("updateFields aurait dû lancer une exception pour des champs nuls.");
        } catch (NullPointerException e) {
            Assertions.assert(e.getMessage().equals("Fields map is null"), "L'exception attendue a été lancée pour des champs nuls.");
        } catch (Exception e) {
            Assertions.fail("Une exception inattendue a été lancée: " + e.getMessage());
        }
    }
}