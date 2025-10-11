import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import database.core.Database;
import database.provider.PostgreSQL;

@ExtendWith(MockitoExtension.class)
public class MainTest {

    @Mock
    private Database mockDatabase;
    
    private Plat plat;

    @BeforeEach
    void setUp() {
        plat = new Plat();
        plat.setDatabase(mockDatabase);
    }

    @Test
    @DisplayName("Test de la connexion à la base de données")
    void testDatabaseConnection() {
        Database db = new PostgreSQL("localhost", "5432", "test_db", "user", "password");
        assertNotNull(db, "La connexion à la base de données ne devrait pas être null");
    }

    @Test
    @DisplayName("Test du comptage des plats")
    void testPlatCount() {
        when(mockDatabase.count(Plat.class)).thenReturn(5L);
        
        long count = plat.count();
        assertEquals(5L, count, "Le nombre de plats devrait être 5");
        verify(mockDatabase).count(Plat.class);
    }

    @Test
    @DisplayName("Test de l'ajout d'un nouveau plat")
    void testAjoutPlat() {
        Plat nouveauPlat = new Plat("Pizza");
        nouveauPlat.setDatabase(mockDatabase);
        
        when(mockDatabase.save(any(Plat.class))).thenReturn(true);
        when(mockDatabase.count(Plat.class)).thenReturn(6L);
        
        assertTrue(nouveauPlat.save(), "La sauvegarde du plat devrait réussir");
        assertEquals(6L, plat.count(), "Le compteur devrait être incrémenté après l'ajout");
    }

    @Test
    @DisplayName("Test avec une base de données invalide")
    void testDatabaseInvalide() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new PostgreSQL("", "", "", "", "");
        });
        
        assertTrue(exception.getMessage().contains("invalide"), 
            "Une exception devrait être levée pour des paramètres de connexion invalides");
    }

    @Test
    @DisplayName("Test de la gestion des erreurs de sauvegarde")
    void testErreurSauvegarde() {
        Plat nouveauPlat = new Plat("Pizza");
        nouveauPlat.setDatabase(mockDatabase);
        
        when(mockDatabase.save(any(Plat.class))).thenThrow(new RuntimeException("Erreur de sauvegarde"));
        
        Exception exception = assertThrows(RuntimeException.class, () -> {
            nouveauPlat.save();
        });
        
        assertEquals("Erreur de sauvegarde", exception.getMessage());
    }

    @Test
    @DisplayName("Test avec un nom de plat null")
    void testPlatNomNull() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Plat(null);
        });
        
        assertNotNull(exception.getMessage(), "Le message d'erreur ne devrait pas être null");
    }

    @Test
    @DisplayName("Test de la modification d'un plat existant")
    void testModificationPlat() {
        Plat platExistant = new Plat("Pizza");
        platExistant.setDatabase(mockDatabase);
        
        when(mockDatabase.update(any(Plat.class))).thenReturn(true);
        
        platExistant.setNom("Pizza Margherita");
        assertTrue(platExistant.save(), "La modification du plat devrait réussir");
        assertEquals("Pizza Margherita", platExistant.getNom());
    }
}