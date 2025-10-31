package database.core;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import java.lang.reflect.Field;

class DBToolTest {

    /**
     * Teste la méthode upperFirst avec une chaîne normale.
     */
    @Test
    void testUpperFirstNormalString() {
        String input = "test";
        String expected = "Test";
        String result = DBTool.upperFirst(input);
        Assertions.assertEquals(expected, result, "La première lettre doit être en majuscule.");
    }

    /**
     * Teste la méthode upperFirst avec une chaîne déjà en majuscule.
     */
    @Test
    void testUpperFirstAlreadyCapitalized() {
        String input = "Test";
        String expected = "Test";
        String result = DBTool.upperFirst(input);
        Assertions.assertEquals(expected, result, "La chaîne doit rester inchangée si elle est déjà capitalisée.");
    }

    /**
     * Teste la méthode upperFirst avec une chaîne vide.
     */
    @Test
    void testUpperFirstEmptyString() {
        String input = "";
        Assertions.assertThrows(StringIndexOutOfBoundsException.class, () -> {
            DBTool.upperFirst(input);
        }, "Une exception doit être lancée pour une chaîne vide.");
    }

    /**
     * Teste la méthode upperFirst avec une chaîne d'un seul caractère.
     */
    @Test
    void testUpperFirstSingleCharacter() {
        String input = "a";
        String expected = "A";
        String result = DBTool.upperFirst(input);
        Assertions.assertEquals(expected, result, "Le caractère unique doit être mis en majuscule.");
    }

    /**
     * Teste la méthode getFieldWithSuperclass avec une classe sans superclasse.
     */
    @Test
    void testGetFieldWithSuperclassNoSuperclass() {
        class NoSuperclass {
            private int field1;
        }

        NoSuperclass instance = new NoSuperclass();
        Field[] fields = DBTool.getFieldWithSuperclass(instance);
        Assertions.assertEquals(0, fields.length, "Aucun champ ne doit être trouvé pour une classe sans superclasse.");
    }

    /**
     * Teste la méthode getFieldWithSuperclass avec une classe ayant une superclasse.
     */
    @Test
    void testGetFieldWithSuperclassWithSuperclass() {
        class Superclass {
            private int superField;
        }

        class Subclass extends Superclass {
            private int subField;
        }

        Subclass instance = new Subclass();
        Field[] fields = DBTool.getFieldWithSuperclass(instance);
        Assertions.assertEquals(1, fields.length, "Un champ de la superclasse doit être trouvé.");
        Assertions.assertEquals("superField", fields[0].getName(), "Le champ trouvé doit être 'superField'.");
    }

    /**
     * Teste la méthode getFieldWithSuperclass avec une classe ayant plusieurs niveaux de superclasses.
     */
    @Test
    void testGetFieldWithSuperclassMultipleSuperclasses() {
        class SuperSuperclass {
            private int superSuperField;
        }

        class Superclass extends SuperSuperclass {
            private int superField;
        }

        class Subclass extends Superclass {
            private int subField;
        }

        Subclass instance = new Subclass();
        Field[] fields = DBTool.getFieldWithSuperclass(instance);
        Assertions.assertEquals(1, fields.length, "Un champ de la super-superclasse doit être trouvé.");
        Assertions.assertEquals("superSuperField", fields[0].getName(), "Le champ trouvé doit être 'superSuperField'.");
    }
}