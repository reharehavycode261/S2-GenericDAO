package database.exception.SQL;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

/**
 * Classe de test pour AttributeTypeNotExistingException.
 */
public class AttributeTypeNotExistingExceptionTest {

    /**
     * Teste le constructeur par défaut de l'exception.
     * Vérifie que le message par défaut est correctement défini.
     */
    @Test
    public void testDefaultConstructor() {
        AttributeTypeNotExistingException exception = new AttributeTypeNotExistingException();
        Assertions.assertEquals("The specified attribute type does not exist.", exception.getMessage(),
                "Le message par défaut de l'exception doit être 'The specified attribute type does not exist.'");
    }

    /**
     * Teste le constructeur avec message personnalisé.
     * Vérifie que le message personnalisé est correctement défini.
     */
    @Test
    public void testConstructorWithCustomMessage() {
        String customMessage = "Custom error message";
        AttributeTypeNotExistingException exception = new AttributeTypeNotExistingException(customMessage);
        Assertions.assertEquals(customMessage, exception.getMessage(),
                "Le message de l'exception doit correspondre au message personnalisé fourni.");
    }

    /**
     * Teste que l'exception est une instance de la classe Exception.
     * Vérifie l'héritage correct de la classe Exception.
     */
    @Test
    public void testExceptionInheritance() {
        AttributeTypeNotExistingException exception = new AttributeTypeNotExistingException();
        Assertions.assertTrue(exception instanceof Exception,
                "AttributeTypeNotExistingException doit être une sous-classe de Exception.");
    }
}