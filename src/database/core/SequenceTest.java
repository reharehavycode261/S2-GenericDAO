import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Assertions;

public class SequenceTest {

    private Sequence sequence;

    @BeforeEach
    public void setUp() {
        // Initialisation d'une instance de Sequence pour les tests
        sequence = new Sequence("PRE", 6, "testSequence");
    }

    @Test
    @DisplayName("Test du constructeur de Sequence")
    public void testSequenceConstructor() {
        // Vérifie que les attributs sont correctement initialisés
        Assertions.assertEquals("PRE", sequence.prefix, "Le préfixe doit être 'PRE'");
        Assertions.assertEquals(6, sequence.length, "La longueur doit être 6");
        Assertions.assertEquals("testSequence", sequence.sequenceName, "Le nom de la séquence doit être 'testSequence'");
    }

    @Test
    @DisplayName("Test de getNextValue - Non implémenté")
    public void testGetNextValueNotImplemented() {
        // Vérifie que la méthode getNextValue retourne null car elle n'est pas implémentée
        Assertions.assertNull(sequence.getNextValue(), "getNextValue doit retourner null car elle n'est pas implémentée");
    }

    @Test
    @DisplayName("Test de getNextValue - Cas de bord avec préfixe vide")
    public void testGetNextValueWithEmptyPrefix() {
        Sequence sequenceWithEmptyPrefix = new Sequence("", 6, "testSequence");
        // Vérifie que la méthode retourne null car elle n'est pas implémentée
        Assertions.assertNull(sequenceWithEmptyPrefix.getNextValue(), "getNextValue doit retourner null car elle n'est pas implémentée");
    }

    @Test
    @DisplayName("Test de getNextValue - Cas de bord avec longueur zéro")
    public void testGetNextValueWithZeroLength() {
        Sequence sequenceWithZeroLength = new Sequence("PRE", 0, "testSequence");
        // Vérifie que la méthode retourne null car elle n'est pas implémentée
        Assertions.assertNull(sequenceWithZeroLength.getNextValue(), "getNextValue doit retourner null car elle n'est pas implémentée");
    }

    @Test
    @DisplayName("Test de getNextValue - Cas de bord avec nom de séquence vide")
    public void testGetNextValueWithEmptySequenceName() {
        Sequence sequenceWithEmptyName = new Sequence("PRE", 6, "");
        // Vérifie que la méthode retourne null car elle n'est pas implémentée
        Assertions.assertNull(sequenceWithEmptyName.getNextValue(), "getNextValue doit retourner null car elle n'est pas implémentée");
    }
}