package database.core;

import database.exception.SQL.AttributeMissingException;
import database.exception.SQL.AttributeTypeNotExistingException;
import database.exception.object.NotIdentifiedInDatabaseException;

import java.sql.SQLException;
import java.util.List;

public class GenericDAO {
    String id;
    private static final String CACHE_KEY_PREFIX = "GenericDAO_";

    // Exemple de méthode d'obtention de données avec cache
    public List<Object> getData(DBConnection dbConnection, String query) throws SQLException {
        String cacheKey = CACHE_KEY_PREFIX + query;
        List<Object> result = (List<Object>) dbConnection.getCachedResult(cacheKey);
        if (result == null) {
            // Supposons que getQueryResult est une méthode qui exécute la requête et obtient les résultats
            result = getQueryResult(dbConnection, query);
            dbConnection.setCachedResult(cacheKey, result);
        }
        return result;
    }

    public void create(DBConnection dbConnection, Object data) throws SQLException {
        // Logique d'insertion
        invalidateCache(dbConnection);
    }

    public void update(DBConnection dbConnection, Object data) throws SQLException {
        // Logique de mise à jour
        invalidateCache(dbConnection);
    }

    public void delete(DBConnection dbConnection, Object data) throws SQLException {
        // Logique de suppression
        invalidateCache(dbConnection);
    }

    private void invalidateCache(DBConnection dbConnection) {
        dbConnection.clearAllCache(); // On invalide le cache pour toutes les opérations affectant les données
    }
}