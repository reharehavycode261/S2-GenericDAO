package database.core;

/**
 * La classe Sequence est utilisée pour gérer les séquences dans une base de données.
 */
public class Sequence {
    // Préfixe de la séquence.
    String prefix;
    // Longueur maximale de la séquence.
    int length;
    // Nom de la séquence en base de données.
    String sequenceName;

    /**
     * Exemple d'utilisation avec ORACLE pour créer une séquence :
     * CREATE SEQUENCE moneySequence START WITH 1 INCREMENT BY 1;
     *
     * Une fonction PL/SQL sous ORACLE pour tester la séquence :
     * CREATE OR REPLACE FUNCTION moneySequenceTest(length IN NUMBER, prefix IN VARCHAR2)
     * RETURN VARCHAR
     * IS result VARCHAR(255);
     * BEGIN
     * RETURN prefix || LPAD(moneySequence.nextval, length, '0');
     * END;
     *
     * Requête pour obtenir la valeur de la séquence formatée :
     * SELECT moneySequenceTest(6, 'STR') FROM DUAL;
     */
    public Sequence(String prefix, int length, String sequenceName) {
        this.prefix = prefix;
        this.length = length;
        this.sequenceName = sequenceName;
    }
    
    // Génère et retourne une valeur de séquence formatée avec le préfixe spécifié et la longueur.
    public String getNextValue() {
        // Logique pour obtenir la prochaine valeur de la séquence.
        // Actuellement non implémentée.
        return null;
    }
}