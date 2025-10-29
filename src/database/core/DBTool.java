package database.core;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * DBTool fournit des méthodes utilitaires pour la manipulation des données et des objets.
 */
public class DBTool {

    /**
     * Met en majuscule la première lettre de la chaîne donnée.
     * @param string la chaîne à transformer.
     * @return la chaîne avec la première lettre en majuscule.
     */
    public static String upperFirst(String string) {
        return string.substring(0, 1).toUpperCase() + string.substring(1);
    }

    /**
     * Récupère tous les champs déclarés dans la classe de l'objet et ses superclasses.
     * @param object l'objet dont les champs doivent être récupérés.
     * @return un tableau de champs trouvés.
     */
    public static Field[] getFieldWithSuperclass(Object object) {
        List<Field> fieldList = new ArrayList<>(List.of(object.getClass().getSuperclass().getDeclaredFields()));
        // Ajouter ici la logique pour inclure les champs de la classe elle-même.
        return fieldList.toArray(new Field[0]); // Convertit la liste en un tableau.
    }
}