package database.exception.SQL;

/**
 * L'exception AttributeTypeNotExistingException est levée lorsque le type d'attribut
 * spécifié dans une requête ou une opération de base de données n'existe pas.
 */
public class AttributeTypeNotExistingException extends Exception {

    /**
     * Constructeur par défaut pour l'exception.
     */
    public AttributeTypeNotExistingException() {
        super("The specified attribute type does not exist.");
    }

    /**
     * Constructeur avec un message personnalisé pour l'exception.
     * 
     * @param message le message d'erreur à afficher.
     */
    public AttributeTypeNotExistingException(String message) {
        super(message);
    }
}