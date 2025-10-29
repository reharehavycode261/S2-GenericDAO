import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

public class ConfigTest {

    /**
     * Teste le constructeur et les getters avec des valeurs valides.
     */
    @Test
    public void testConfigInitializationAndGetters() {
        String url = "jdbc:mysql://localhost:3306/mydb";
        String username = "user";
        String password = "pass";

        Config config = new Config(url, username, password);

        Assertions.assertEquals(url, config.getUrl(), "L'URL devrait être correctement initialisée et retournée.");
        Assertions.assertEquals(username, config.getUsername(), "Le nom d'utilisateur devrait être correctement initialisé et retourné.");
        Assertions.assertEquals(password, config.getPassword(), "Le mot de passe devrait être correctement initialisé et retourné.");
    }

    /**
     * Teste le constructeur avec des valeurs nulles pour vérifier la gestion des cas limites.
     */
    @Test
    public void testConfigInitializationWithNullValues() {
        Config config = new Config(null, null, null);

        Assertions.assertNull(config.getUrl(), "L'URL devrait être null lorsque initialisée avec une valeur null.");
        Assertions.assertNull(config.getUsername(), "Le nom d'utilisateur devrait être null lorsque initialisé avec une valeur null.");
        Assertions.assertNull(config.getPassword(), "Le mot de passe devrait être null lorsque initialisé avec une valeur null.");
    }

    /**
     * Teste le constructeur avec des chaînes vides pour vérifier la gestion des cas limites.
     */
    @Test
    public void testConfigInitializationWithEmptyStrings() {
        Config config = new Config("", "", "");

        Assertions.assertEquals("", config.getUrl(), "L'URL devrait être une chaîne vide lorsque initialisée avec une chaîne vide.");
        Assertions.assertEquals("", config.getUsername(), "Le nom d'utilisateur devrait être une chaîne vide lorsque initialisé avec une chaîne vide.");
        Assertions.assertEquals("", config.getPassword(), "Le mot de passe devrait être une chaîne vide lorsque initialisé avec une chaîne vide.");
    }
}