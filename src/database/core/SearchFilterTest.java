import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

public class SearchFilterTest {

    /**
     * Teste la création d'un SearchFilter avec des valeurs valides.
     */
    @Test
    public void testSearchFilterCreation() {
        SearchFilter filter = new SearchFilter("name", "equals", "John Doe");
        Assertions.assertNotNull(filter, "Le filtre ne doit pas être null après la création.");
    }

    /**
     * Teste la méthode getFieldName pour s'assurer qu'elle retourne le nom du champ correct.
     */
    @Test
    public void testGetFieldName() {
        SearchFilter filter = new SearchFilter("name", "equals", "John Doe");
        Assertions.assertEquals("name", filter.getFieldName(), "Le nom du champ doit être 'name'.");
    }

    /**
     * Teste la méthode getOperator pour s'assurer qu'elle retourne l'opérateur correct.
     */
    @Test
    public void testGetOperator() {
        SearchFilter filter = new SearchFilter("age", "greater_than", 30);
        Assertions.assertEquals("greater_than", filter.getOperator(), "L'opérateur doit être 'greater_than'.");
    }

    /**
     * Teste la méthode getValue pour s'assurer qu'elle retourne la valeur correcte.
     */
    @Test
    public void testGetValue() {
        SearchFilter filter = new SearchFilter("age", "greater_than", 30);
        Assertions.assertEquals(30, filter.getValue(), "La valeur doit être 30.");
    }

    /**
     * Teste la création de SearchFilter avec une valeur null pour le nom du champ.
     */
    @Test
    public void testSearchFilterWithNullFieldName() {
        SearchFilter filter = new SearchFilter(null, "equals", "John Doe");
        Assertions.assertNull(filter.getFieldName(), "Le nom du champ doit être null.");
    }

    /**
     * Teste la création de SearchFilter avec une valeur null pour l'opérateur.
     */
    @Test
    public void testSearchFilterWithNullOperator() {
        SearchFilter filter = new SearchFilter("name", null, "John Doe");
        Assertions.assertNull(filter.getOperator(), "L'opérateur doit être null.");
    }

    /**
     * Teste la création de SearchFilter avec une valeur null pour la valeur.
     */
    @Test
    public void testSearchFilterWithNullValue() {
        SearchFilter filter = new SearchFilter("name", "equals", null);
        Assertions.assertNull(filter.getValue(), "La valeur doit être null.");
    }

    /**
     * Teste la création de SearchFilter avec des valeurs vides pour le nom du champ et l'opérateur.
     */
    @Test
    public void testSearchFilterWithEmptyValues() {
        SearchFilter filter = new SearchFilter("", "", "");
        Assertions.assertEquals("", filter.getFieldName(), "Le nom du champ doit être une chaîne vide.");
        Assertions.assertEquals("", filter.getOperator(), "L'opérateur doit être une chaîne vide.");
        Assertions.assertEquals("", filter.getValue(), "La valeur doit être une chaîne vide.");
    }
}